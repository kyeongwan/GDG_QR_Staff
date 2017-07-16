var https = require('https');
var http = require('http');
var fs = require('fs');
var path = require('path');
var port = process.env.PORT || 3000; //*

http.createServer(function (request, response) {
    console.log('request starting...');

    var access_Token = "";
    var filePath = '.' + request.url;
    if (filePath == './')
        filePath = './qr_check.html';

    if (filePath.indexOf("./OAuth/") == 0){
        console.log(filePath);
        var https2 = require('https'); // http를 https로 고쳤습니다.
        var request = https2.request({
            hostname: 'api.meetup.com',
            path: '/2/member/self/',
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + filePath.substring(8),
                'Content-Type' : 'application/json'
            }
        }, function(response2) {
            var body = "";
            response2.on('data', function(contents) {
                body += contents;
            });
            response2.on('end', function() {
                console.log(response2.statusCode);
                console.log(JSON.parse(body.toString())); // JSON 파싱을 추가했습니다.
                access_Token = JSON.parse(body.toString()).id;
                var name = JSON.parse(body.toString()).name;
                response.writeHead(301,
                  {Location: 'http://lemonlab.co.kr/gdg/qrcode.html?mail='+access_Token + '&code='+name} 
                );
                response.end();
            });

        });
        request.on('error', function(e) {
            console.log("Error!", e.message);
        });
        
        request.end();
    }
    else {


        var extname = path.extname(filePath);
        var contentType = 'text/html';
        switch (extname) {
            case '.js':
                contentType = 'text/javascript';
                break;
            case '.css':
                contentType = 'text/css';
                break;
            case '.json':
                contentType = 'application/json';
                break;
            case '.png':
                contentType = 'image/png';
                break;      
            case '.jpg':
                contentType = 'image/jpg';
                break;
            case '.svg':
                contentType = 'image/svg+xml';
                break;
            case '.wav':
                contentType = 'audio/wav';
                break;
        }

        fs.readFile(filePath, function(error, content) {
            if (error) {
                if(error.code == 'ENOENT'){
                    fs.readFile('./404.html', function(error, content) {
                        response.writeHead(200, { 'Content-Type': contentType });
                        response.end(content, 'utf-8');
                    });
                }
                else {
                    response.writeHead(500);
                    response.end('Sorry, check with the site admin for error: '+error.code+' ..\n');
                    response.end(); 
                }
            }
            else {
                response.writeHead(200, { 'Content-Type': contentType });
                response.end(content, 'utf-8');
            }
        });
    }

}).listen(port);