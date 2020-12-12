const async = require('async');

function printAsync(s, cb) {
  var delay = Math.floor((Math.random() * 1000) + 500);
  setTimeout(function () {
    console.log(s);
    if (cb) cb();
  }, delay);
}

function task(n) {
  return new Promise((resolve, reject) => {
    printAsync(n, function () {
      resolve(n);
    });
  });
}

  // need callback here to iterate m times
const iteration = (callback) => {
      task(1).then((n) => {
          console.log('task', n, 'done');
          return task(2);
      }).then((n) => {
          console.log('task', n, 'done');
          return task(3);
      }).then((n) => {
          console.log('task', n, 'done');
          console.log('done');
          callback();
      });
  };


/*
** Zadanie:
** Napisz funkcje loop(m), ktora powoduje wykonanie powyzszej
** sekwencji zadan m razy.
**
*/

function loop(m){
    iterations = []
    for(let i = 0; i < m; i++)
        iterations.push(iteration)
    async.waterfall(iterations)
}

loop(4);
