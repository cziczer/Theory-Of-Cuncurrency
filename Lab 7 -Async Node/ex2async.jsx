const walkdir = require('walkdir');
const fs = require('fs');

async function linesInSingleFile(file, callback) {
    let count = 0;
    fs.createReadStream(file).on('data', function(chunk) {
        count = chunk.toString('utf8')
        .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
        .length-1;
    }).on('end', function() {
        totalLines += count;
        console.log(file, count);
        callback()
    }).on('error', function(err) {
        console.error(err);
        callback()
    });
}

function countLines(rootPath) {
    const paths = walkdir.sync(rootPath);
    let files = paths.length;
    const startTime = new Date().getTime()
    walkdir.sync(rootPath).map(path => linesInSingleFile(path, () => {
        files--;
        if (files == 0) {
            console.log("Total time: ", new Date().getTime() - startTime);
            console.log("Lines: ", totalLines)
        }
    }))
}

let totalLines = 0;
countLines('./PAM08');