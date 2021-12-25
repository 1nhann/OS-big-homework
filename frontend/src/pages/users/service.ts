// @ts-nocheck
import { extend } from 'umi-request';
import axios from 'axios';

interface codeMapValues {
  [name: string]: any;
}

const baseUrl = 'http://wsl.com:8883/';

const errorHandler = function(error: any) {
  const codeMap: codeMapValues = {
    PUT: '修改记录出错',
    DELETE: '删除记录出错',
    POST: '新增记录出错',
    GET: '获取数据出错',
  };
  if (error.response) {
    console.log(error.response.status);
    console.log(error.data);
    console.log(error.request.options.method);
    if (error.response.status > 400) {
      // message.error(
      //   error.data.message
      //     ? codeMap[error.request.options.method] + error.data.message
      //     : codeMap[error.request.options.method] + error.data,
      // );
    } else {
      // message.success(error.data);
    }
  } else {
    // message.error('网络错误');
  }
  throw error;
};
const extendRequest = extend({
  timeout: 1000,
  headers: {
    // "Access-Control-Allow-Origin": "*"
  },
  errorHandler,
});

// 磁盘初始化
const distSpaceInit = async () => {
  return extendRequest(`${baseUrl}DiskSpaceInit`, {
    method: 'post',
  })
    .then(response => {
      return response;
    })
    .catch(error => {
      return false;
    });
};

// 磁盘打印
const getDistState = async () => {
  return extendRequest(`${baseUrl}DiskState`, {
    method: 'post',
  })
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 显示目录结构
const displayFileSystemStructure = async ({ foldername, num }) => {
  return extendRequest(
    `${baseUrl}DisplayFileSystemStructure?foldername=${foldername}&num=${num}`,
    {
      method: 'get',
    },
  )
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 新建目录
const createFolder = async ({ current_name, name, num }) => {
  return extendRequest(
    `${baseUrl}DirectoryCreate?parentFolderName=${current_name}&thisFolderName=${name}&num=${num}`,
    {
      method: 'get',
      // data:{
      //   parentFolderName: current_name,
      //   thisFolderName:name,
      //   num:num
      // }
    },
  )
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 删除目录
const deleteFolder = async ({ delete_name, num }) => {
  return extendRequest(
    `${baseUrl}DirectoryDelete?folderName=${delete_name}&num=${num}`,
    {
      method: 'get',
      // data:{
      //   folderName:delete_name,
      //   num:num
      // }
    },
  )
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 新建文件
const createFile = async ({ current_folder_name, file_name, user, num }) => {
  return extendRequest(
    `${baseUrl}FileMake?directoryName=${current_folder_name}&fileName=${file_name}&owner=${user}&num=${num}&content=''`,
    {
      method: 'get',
      // data:{
      //   directoryName: current_folder_name,
      //   filename: file_name,
      //   owner: user,
      //   num:num,
      //   content:''
      // }
    },
  )
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 删除文件
const deleteFile = async ({ delete_name }) => {
  return extendRequest(`${baseUrl}FileDelete?fileName=${delete_name}`, {
    method: 'get',
    // data:{
    //   fileName:delete_name
    // }
  })
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 修改文件内容
const changeFileContent = async ({ change_file_name, content }) => {
  return extendRequest(
    `${baseUrl}EditFileContent?content=${content}&fileName=${change_file_name}`,
    {
      method: 'get',
      // data:{
      //   content: content,
      //   fileName: change_file_name
      // }
    },
  )
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 显示文件内容
const readFileContent = async ({ file_name }) => {
  return axios
    .get(`${baseUrl}ReadFileContent?fileName=${file_name}`, { timeout: 300000 })
    .then((resp: any) => {
      // console.log(resp.data);
      // // console.log(eval(resp.data));
      // console.log(JSON.parse(resp.data));
      // return JSON.parse(resp.data);
      return resp.data;
    })
    .catch((error: any) => {
      console.error(error);
      return false;
    });
  // return new Promise((resolve,reject)=>{
  //   setTimeout(() => {
  //     resolve('233')
  //   }, 2000);
  // })
  return extendRequest(`${baseUrl}ReadFileContent?fileName=${file_name}`, {
    method: 'get',
    // data:{
    //   fileName: file_name
    // }
  })
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};

// 获取内存块状态
const getBlock = async () => {
  return extendRequest(`${baseUrl}block/returnTemp`, {
    method: 'get',
  })
    .then(response => {
      return response;
    })
    .catch(error => {
      console.log(error);
      return false;
    });
};



export {
  distSpaceInit,
  getDistState,
  displayFileSystemStructure,
  createFolder,
  deleteFolder,
  createFile,
  deleteFile,
  changeFileContent,
  readFileContent,
  getBlock,
};
