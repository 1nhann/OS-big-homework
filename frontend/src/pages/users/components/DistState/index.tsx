import React from 'react';
import styles from './distState.less';
interface Props {
  data: any;
  title: string;
}

const colsNumber = 32;

function DistState(props: Props) {
  const { data, title } = props;
  const length = data.length;
  const rowsNumber = Math.ceil(length / colsNumber);
  let i = 0;

  return (
    <div className={styles.wrap}>
      <div className={styles.title}>{title}</div>
      <table className={styles.table}>
        <thead>
          <tr>
            {new Array(colsNumber + 1)
              .fill('')
              .map((e, index) =>
                index == 0 ? (
                  <th key={index}></th>
                ) : (
                  <th key={index}>{index}</th>
                ),
              )}
          </tr>
        </thead>
        <tbody>
          {new Array(rowsNumber).fill('').map((value, index) => (
            <tr key={index}>
              <th>{index + 1}</th>
              {new Array(colsNumber).fill('').map((v, index2) => {
                if (i >= length) {
                  return null;
                } else {
                  i++;
                  return (
                    <th className={styles[`color-${data[i - 1]}`]} key={index2}>
                      {data[i - 1] > 0 ? 1 : 0}
                    </th>
                  );
                }
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default DistState;
