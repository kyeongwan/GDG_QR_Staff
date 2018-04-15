var https = require('https');
var http = require('http');
var fs = require('fs');
var path = require('path');
var meetupRequest = require('request');
var port = process.env.PORT || 3000; //*

http.createServer(function (request, response) {
    console.log('request starting...');

    console.log("URL : " + request.url);  
    var requestBody = "";
    request.on('data', function(contents) {
        requestBody += contents;
    });
    request.on('end', function() {
        console.log(requestBody);
    });

    var access_Token = "";
    var filePath = '.' + request.url;
    if (filePath == './')
        filePath = './qr_check.html';

    if(filePath == './admin')
        filePath = './test.html';

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
        }, function(meetupResponse) {
            var body = "";
            meetupResponse.on('data', function(contents) {
                body += contents;
            });
            meetupResponse.on('end', function() {
                console.log(meetupResponse.statusCode);
                console.log(JSON.parse(body.toString())); // JSON 파싱을 추가했습니다.
                access_Token = JSON.parse(body.toString()).id;
                response.writeHead(301,
                  {Location: 'http://lemonlab.co.kr/gdg/qrcode.html?mail='+access_Token} 
                );
                response.end();
            });
        });
        request.on('error', function(e) {
            console.log("Error!", e.message);
        });
        
        request.end();
    } else if(filePath.indexOf("./api") == 0) {

        
        var param = function(url){
            let q= url.split('?'),result={};
            if(q.length>=2){
                q[1].split('&').forEach((item)=>{
                   try {
                     result[item.split('=')[0]]= item.split('=')[1];
                   } catch (e) {
                     result[item.split('=')[0]]='';
                   }
                })
            }
            return result;
        }
        var paramMap = param(request.url);
        console.log(paramMap);

        meetupRequest.get('http://api.meetup.com/' + paramMap['groupName'] +'/events/' + paramMap['eventId'] + '/rsvps?key=' + paramMap['apiKey']+ '&fields=answers,pay_status&response=yes', function (error, response2, body) {
        console.log('error:', error); //           console.log('statusCode:', response2 && response2.statusCode); // Print the response status code if a response was received
            // Print the error if one occurred
          // console.log('body:', body); // Print the HTML for the Google homepage.

        
        var result = new Array();
        if(response2.statusCode == 401) {
            var object = new Object();
            object.code = "auth error";
            result.push(object);
            response.writeHead(401, {'Content-Type' : 'application/json; charset=utf-8'});
            response.end(JSON.stringify(result), 'utf-8');
            return
        }

          var result = new Array();
          var obj = JSON.parse(body);
          console.log(obj.length);
          for (var i = 0; i < obj.length; i++) {
            var object = new Object();
            object.name = obj[i].member.name;
            object.userID = obj[i].member.id;
            object.rsvps = obj[i].response;
            object.rsvpedOn = obj[i].created;
            object.price = obj[i].pay_status;
            try{
                object.photo = obj[i].member.photo.thumb_link;   
            }catch(err){
                object.photo = "";
            }

            try {
                object.price = obj[i].pay_status;
            }catch(err) {
                object.price = "";
            }

            try{
                object.answer = obj[i].answers[0].answer;
            }catch(err){
                object.answer = "";
            }
            console.log(object);
            result.push(object);
          }


                response.writeHead(200, {'Content-Type' : 'application/json; charset=utf-8'});
                response.end(JSON.stringify(result), 'utf-8');
          
        });

        // var http2 = require('http');
        // var request = http2.request({
        //     hostname: 'api.meetup.com',
        //     path: '/GDG-Campus/events/242668148/rsvps?key=9227e7f83811132d1e5e6a42197510',
        //     method : 'GET'
        // }, function(response2) {
        //     var body = "";
        //     console.log("bbb");
        //     response2.on('data', function(contents) {
        //         body += contents;
        //     });

        //     console.log(body);

        //     response2.on('end', function() {
        //         console.log(response2.statusCode);
        //         response.writeHead(200, {'Content-Type' : 'application/json'});
        //         response.end(body, 'utf-8');
        //     });
        // });
        //  request.on('error', function(e) {
        //     console.log("Error!", e.message);
        // });

        // console.log("aaa");
    }else if(filePath.indexOf("./rsvp") == 0){
        var jsonData = "";
        request.on('data', function (chunk) {
            jsonData += chunk;
        });
        request.on('end', function () {
            console.log(jsonData);
            var reqObj = JSON.parse(jsonData);
            console.log(reqObj);
            reqObj = JSON.parse(reqObj);
            var member = reqObj.member;
            console.log(member);
            var status = reqObj.status;
            console.log(status);

            var groupName = reqObj.groupName;
            console.log(status);
            var eventId = reqObj.eventId;
            console.log(status);
            var apiKey = reqObj.apiKey;
            console.log(status);

             
            meetupRequest.post({ 
                url: 'http://api.meetup.com/' + groupName +'/events/' + eventId + '/attendance?key=' + apiKey,
                form:    {member : reqObj.member, status: reqObj.status},
                headers: {
                    'Content-Type' : 'application/x-www-form-urlencoded' 
                    
                },
                
                json: true },
                function (error333, response333, body333) {
                    console.log(body333)
                    response.writeHead(200);
                    response.end(JSON.stringify(body333));
                
            })
        });
                

    }else {
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
