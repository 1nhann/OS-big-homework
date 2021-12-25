// @ts-nocheck
import { Reducer, Effect, Subscription } from 'umi';
import { getDistState } from './service';
import { message } from 'antd';

const modelData = {
  namespace: 'users',
  state: {
    distState: {},
    curCatalog: ['root'],
  },
  reducers: {
    setState(state, { payload }) {
      return { ...state, ...payload };
    },
  },
  effects: {
    *getDisState({ payload }, { put, call, select }) {
      const data = yield call(getDistState);
      yield put({
        type: 'setState',
        payload: {
          distState: data,
        },
      });
    },
  },
  subscriptions: {
    setup({ dispatch, history }) {
      return history.listen(({ pathname }) => {
        if (pathname === '/users') {
          dispatch({
            type: 'getRemote',
            payload: {
              page: 1,
              per_page: 10,
            },
          });
        }
      });
    },
  },
};

export default modelData;
