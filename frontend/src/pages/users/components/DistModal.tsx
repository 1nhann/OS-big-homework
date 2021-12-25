import React from 'react';
import DistState from './DistState';
import styles from './DistState/distState.less';
interface Props {
  dataSource: any;
}

function DistModal(props: Props) {
  const { dataSource } = props;

  return (
    <div style={{ width: 660 }}>
      <div style={{ display: 'flex', marginLeft: 20, marginTop: 10 }}>
        <div
          className={styles['color-0']}
          style={{ width: 20, height: 20 }}
        ></div>
        <div style={{ marginRight: 20 }}>未使用</div>
        <div
          className={styles['color-1']}
          style={{ width: 20, height: 20 }}
        ></div>
        <div style={{ marginRight: 20 }}>数据占用</div>
        <div
          className={styles['color-2']}
          style={{ width: 20, height: 20 }}
        ></div>
        <div style={{ marginRight: 20 }}>一级索引占用</div>


        <div
          className={styles['color-3']}
          style={{ width: 20, height: 20 }}
        ></div>
        <div style={{ marginRight: 20 }}>二级索引占用</div>


        <div
          className={styles['color-4']}
          style={{ width: 20, height: 20 }}
        ></div>
        <div style={{ marginRight: 20 }}>目录占用</div>
      </div>
      <div style={{ width: 660 }}>
        <DistState
          data={dataSource?.ordblock || []}
          title="位示图（普通盘块）"
        />
      </div>
      <div style={{ width: 660 }}>
        <DistState
          data={dataSource?.exblock || []}
          title="位示图（兑换区盘块）"
        />
      </div>
    </div>
  );
}

export default DistModal;
