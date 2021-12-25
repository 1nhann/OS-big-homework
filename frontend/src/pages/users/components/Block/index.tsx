// @ts-nocheck
import React, { useEffect, useState } from 'react';
import { getBlock } from '../../service';
import styles from './index.less';

interface Props {}

function colorById(i) {
  if (i < 10) i = i * 302.3;
  if (i < 100) i = i * 31.2;
  for (; i > 255; i *= 0.98);
  var temp = i.toString().substring(i.toString().length - 3);
  i += parseInt(temp);
  for (; i > 255; i -= 255);
  i = parseInt(i);
  if (i < 10) i += 10;

  var R = i * (i / 100);
  for (; R > 255; R -= 255);
  if (R < 50) R += 60;
  R = parseInt(R).toString(16);

  var G = i * (i % 100);
  for (; G > 255; G -= 255);
  if (G < 50) G += 60;
  G = parseInt(G).toString(16);

  var B = i * (i % 10);
  for (; B > 255; B -= 255);
  if (B < 50) B += 60;
  B = parseInt(B).toString(16);

  console.log(i + ':' + R + ':' + G + ':' + B);
  return '#' + R + G + B;
}

function Index(props: Props) {
  const [data, setData] = useState([]);
  useEffect(() => {
    init();
  }, []);
  const init = async () => {
    const data = await getBlock();
    setData(data);
    setTimeout(() => {
      init();
    }, 200);
  };

  let index = 0;

  return (
    <div className={styles['wrap']}>
      <div className={styles['title']}>内存使用情况</div>
      <table>
        <thead>
          <tr>
            <th></th>
            {new Array(8).fill('').map((v, i) => {
              return <th>{i + 1}</th>;
            })}
          </tr>
        </thead>
        <tbody>
          {new Array(8).fill('').map((value, i) => {
            return (
              <tr key={i}>
                <th>{i + 1}</th>
                {new Array(8).fill('').map((v, key) => {
                  index++;
                  if (data[index - 1] == -1 || data[index - 1] == undefined) {
                    return (
                      <th style={{ backgroundColor: 'skyblue' }} key={key}>
                        {data[index - 1]}
                      </th>
                    );
                  } else {
                    return (
                      <th
                        key={key}
                        style={{ backgroundColor: colorById(data[index - 1]) }}
                      >
                        {data[index - 1]}
                      </th>
                    );
                  }
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default Index;
