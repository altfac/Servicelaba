import styles from './Volume.module.css'
import React from 'react'

const Volume = (props) => {
  return (
    <div className={styles.wrapper}>
      <input
        type='range'
        min='0'
        max='100'
        defaultValue='100'
        className={styles.slider}
        id='myRange'
        onChange={props.onChange}
        style={{
          background: `linear-gradient(90deg, orange ${props.value * 100
            }%, red ${props.value * 100}%)`
        }}
      />
    </div>
  )
}

export default Volume
