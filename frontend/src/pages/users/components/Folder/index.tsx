import React, { useState, useEffect } from 'react';
import {
  Button,
  Radio,
  Space,
  Modal,
  Form,
  Input,
  message,
  Empty,
  List,
  Collapse,
} from 'antd';
import { connect } from 'umi';
import { CaretRightOutlined } from '@ant-design/icons';
import {
  displayFileSystemStructure,
  createFolder,
  deleteFolder,
  createFile,
  deleteFile,
  changeFileContent,
  readFileContent,
} from '../../service';
import styles from './index.less';
import FileFilled from '@ant-design/icons/lib/icons/FileFilled';
import FolderOpenFilled from '@ant-design/icons/lib/icons/FolderOpenFilled';

const { Panel } = Collapse;
const { TextArea } = Input;

interface Props {
  curCatalog: any;
  handleCatalog: any;
  dispatch: any;
}

let dispatchList: any = [];
function Index(props: Props) {
  const { curCatalog = [], handleCatalog, dispatch } = props;
  const current_folder_name = curCatalog[curCatalog.length - 1];
  const [curStatus, setCurStatus] = useState(0);
  const [selectedName, setSelectedName] = useState('');
  const [fileList, setFileList] = useState([]);
  const [newFolderVisible, setNewFolderVisible] = useState(false);
  const [newFileVisible, setNewFileVisible] = useState(false);
  const [dispatchingFile, setDispatchingFile] = useState([] as any);
  const [showChangeFileVisible, setShowChangeFileVisible] = useState(false);
  const [fileContent, setFileContent] = useState('');

  useEffect(() => {
    init();
  }, [current_folder_name]);

  const init = async () => {
    const { data = [] }: any = await displayFileSystemStructure({
      foldername: current_folder_name,
      num: curCatalog.length - 1,
    });
    setFileList(data);
  };

  const refreshList = async () => {
    const { data = [] }: any = await displayFileSystemStructure({
      foldername: current_folder_name,
      num: curCatalog.length - 1,
    });
    setFileList(data);
  };

  function onSelect(e: any) {
    const value = e.target.value;
    const reg = /(.*):(.*)/;
    const [, type, name] = reg.exec(value) || [0, 0, 0];
    type === 'folderName' ? setCurStatus(1) : setCurStatus(2);
    setSelectedName(name as string);
  }

  const onCreateNewFolder = async ({ foldername }: any) => {
    await createFolder({
      current_name: current_folder_name,
      name: foldername,
      num: curCatalog.length - 1,
    });
    dispatch({
      type: 'users/getDisState',
    });
    setNewFolderVisible(false);
    refreshList();
  };

  const onDeleteFolder = async () => {
    await deleteFolder({
      delete_name: selectedName,
      num: curCatalog.length,
    });
    dispatch({
      type: 'users/getDisState',
    });
    refreshList();
  };

  const onCreateNewFile = async ({ file_name, user }: any) => {
    await createFile({
      current_folder_name,
      file_name,
      user,
      num: curCatalog.length - 1,
    });
    setNewFileVisible(false);
    dispatch({
      type: 'users/getDisState',
    });
    refreshList();
  };

  const onDeleteFile = async () => {
    await deleteFile({ delete_name: selectedName });
    dispatch({
      type: 'users/getDisState',
    });
    refreshList();
  };

  const openFolder = () => {
    setCurStatus(0);
    let copyCurCatalog = [...curCatalog];
    copyCurCatalog.push(selectedName);
    handleCatalog(copyCurCatalog);
  };

  const dispatchFile = async (index: number) => {
    const {
      content = '',
      date = '',
      user = '',
      filesize = 0, 
    } = await readFileContent({
      file_name: selectedName,
    });
    // console.log(r);
    setDispatchingFile((data: any) => {
      data[index].state = '调入完成';
      data[index].content = content;
      data[index].date = date;
      data[index].user = user;
      data[index].filesize = filesize;
      return [...data];
    });
  };

  return (
    <div>
      <div>
        {curCatalog.length > 1 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={() => {
              let copyCurCatalog = [...curCatalog];
              copyCurCatalog.pop();
              handleCatalog(copyCurCatalog);
              setCurStatus(0);
            }}
          >
            返回上一级
          </Button>
        )}
        <Button
          type="primary"
          shape="round"
          style={{ marginRight: 10 }}
          onClick={() => {
            setNewFolderVisible(true);
          }}
        >
          新建目录
        </Button>
        <Button
          type="primary"
          shape="round"
          style={{ marginRight: 10 }}
          onClick={() => {
            setNewFileVisible(true);
          }}
        >
          新建文件
        </Button>
        {curStatus === 1 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={() => {
              openFolder();
            }}
          >
            打开目录
          </Button>
        )}
        {curStatus === 1 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={() => {
              onDeleteFolder();
            }}
          >
            删除目录
          </Button>
        )}
        {curStatus === 2 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={() => {
              let dispatchingFileCopy = [...dispatchingFile];
              const index =
                dispatchingFileCopy.push({
                  filename: selectedName,
                  state: '调入中',
                  content: '',
                  filesize: 0,
                  user: '',
                  date: '',
                }) - 1;
              setDispatchingFile(dispatchingFileCopy);
              dispatchList = dispatchingFileCopy;
              dispatchFile(index);
            }}
          >
            读文件
          </Button>
        )}
        {curStatus === 2 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={async () => {
              setShowChangeFileVisible(true);
              setFileContent('');
            }}
          >
            写文件
          </Button>
        )}
        {curStatus === 2 && (
          <Button
            type="primary"
            shape="round"
            style={{ marginRight: 10 }}
            onClick={() => {
              onDeleteFile();
            }}
          >
            删除文件
          </Button>
        )}
      </div>

      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          width: 600,
          marginTop: 15,
        }}
      >
        {/* 文件list */}
        <Radio.Group onChange={onSelect}>
          <Space direction="vertical">
            {fileList.map(({ folderName = '', filename = '' }, key) => {
              if (folderName) {
                return (
                  <Radio key={key} value={`folderName:${folderName}`}>
                    <FolderOpenFilled />
                    &nbsp;&nbsp;
                    {folderName}
                  </Radio>
                );
              } else {
                return (
                  <Radio key={key} value={`filename:${filename}`}>
                    <FileFilled /> &nbsp;&nbsp;
                    {filename}
                  </Radio>
                );
              }
            })}
          </Space>
        </Radio.Group>

        {/* 工作目录 */}
        <Collapse
          bordered={true}
          defaultActiveKey={['1']}
          expandIcon={({ isActive }) => (
            <CaretRightOutlined rotate={isActive ? 90 : 0} />
          )}
          className="site-collapse-custom-collapse"
          style={{ width: 250, height: '100%' }}
        >
          <Panel
            key="-1"
            showArrow={false}
            collapsible="disabled"
            header="工作目录"
          ></Panel>
          {dispatchingFile.map((v: any, i: number) => (
            <Panel
              header={`${v.filename} ------- ${v.state}`}
              key={i}
              className="site-collapse-custom-panel"
            >
              <p>内容：{v.content}</p>
              <p>创建人：{v.user}</p>
              <p>大小（字节）：{v.filesize}</p>
            </Panel>
          ))}
        </Collapse>

        {/* <List
          size="small"
          header={<div>工作目录</div>}
          bordered
          style={{ width: 200 }}
          dataSource={dispatchingFile}
          renderItem={(item: any) => (
            <List.Item>
              {item.filename} ------------- {item.state}
              <br />
              {item.content}
            </List.Item>
          )}
        /> */}
      </div>

      {/* 新建目录modal */}
      <Modal
        title="新建目录"
        visible={newFolderVisible}
        footer={null}
        onCancel={() => setNewFolderVisible(false)}
      >
        <Form
          name="basic"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          onFinish={onCreateNewFolder}
          autoComplete="off"
        >
          <Form.Item
            label="目录名"
            name="foldername"
            rules={[
              { required: true, message: 'Please input your foldername!' },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* 新建文件modal */}
      <Modal
        title="新建文件"
        visible={newFileVisible}
        footer={null}
        onCancel={() => setNewFileVisible(false)}
      >
        <Form
          name="basic"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          onFinish={onCreateNewFile}
          autoComplete="off"
        >
          <Form.Item
            label="文件名"
            name="file_name"
            rules={[{ required: true, message: 'Please input your filename!' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="创建者"
            name="user"
            rules={[{ required: true, message: 'Please input your username!' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      {/* 修改文件内容modal */}
      <Modal
        className={styles['change-content-wrap']}
        title={`修改${selectedName}`}
        visible={showChangeFileVisible}
        footer={null}
        onCancel={() => {
          setShowChangeFileVisible(false);
        }}
      >
        <div style={{ overflow: 'hidden' }}>
          <TextArea
            rows={4}
            defaultValue={fileContent}
            onChange={e => {
              setFileContent(e.target.value);
            }}
          />
          <Button
            type="primary"
            style={{ float: 'right', marginTop: 8 }}
            onClick={async () => {
              const { code, msg } = await changeFileContent({
                change_file_name: selectedName,
                content: fileContent,
              });
              dispatch({
                type: 'users/getDisState',
              });
              setShowChangeFileVisible(false);
            }}
          >
            保存
          </Button>
        </div>
      </Modal>
    </div>
  );
}

export default connect()(Index);
