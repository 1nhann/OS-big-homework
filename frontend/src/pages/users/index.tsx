// @ts-nocheck
import React, { FC, useEffect } from 'react';
import { connect, Dispatch, Loading, UserState } from 'umi';
import { distSpaceInit, getDistState } from './service';
import DistModal from './components/DistModal';
import Folder from './components/Folder';
import Block from './components/Block';

interface UserPageProps {
  users: UserState;
  dispatch: Dispatch;
  usersListLoading: boolean;
}
const UsersListPage: FC<UserPageProps> = ({
  users,
  dispatch,
  usersListLoading,
}) => {
  const { distState, curCatalog } = users;
  useEffect(() => {
    init();
  }, []);

  const init = async () => {
    // await distSpaceInit();
    dispatch({
      type: 'users/getDisState',
    });
  };
  const handleCatalog = logList => {
    dispatch({
      type: 'users/setState',
      payload: {
        curCatalog: logList,
      },
    });
  };
  // console.log('spcming', users);
  return (
    <div style={{ display: 'flex' }}>
      <DistModal dataSource={distState} />
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          paddingTop: 30,
          marginLeft: 20,
        }}
      >
        <Folder curCatalog={curCatalog} handleCatalog={handleCatalog} />
        <Block />
      </div>
    </div>
  );
};

const mapStateToProps = ({
  users,
  loading,
}: {
  users: UserState;
  loading: Loading;
}) => {
  return {
    users,
    usersListLoading: loading.models.users,
  };
};
export default connect(mapStateToProps)(UsersListPage);
