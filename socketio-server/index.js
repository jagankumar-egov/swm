var ontime = require('ontime');
var moment = require('moment-timezone');
var socketio = require('socket.io');
var fs = require('fs');
var http = require('http');
var CronJob = require('cron').CronJob;


var io = socketio.listen(3005);

// creating namespaces
var location_nsp = io.of('/location');

var PARTNER_DICT = {};

//for storing movement in km for every user
var movement_in_km = {};

//for storing retailers list for every user
var retailers_visited = {};

var user_info;

var GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0, "distance_individual": movement_in_km, "retailers_individual":retailers_visited};



//for storing data as key-value pair like { "customerID1": [[.....],[.......]], "customerID2": [[.......],[.....]] }
var whole_day_data = {};

// for storing latest data for all customers like { "customer_ID1": [] ,........ "customer_ID10": []....}
var latest_data_for_all = {};

location_nsp.on('connection', function(socket) {

    //on connection emit latest data to client for all customers
    socket.emit('customer_list', latest_data_for_all);
    //socket.emit('global_params',GLOBAL_PARAMS);

    console.log("New User Connected");

    //joining rooms
    socket.on('room', function(room) {
        //when selecting one customerID , we add that client to the customer's room and also send json for that customer

        socket.join(room);
        console.log("Joined room :- " + room)
        // sending whole day data to that socket to draw heatmap and polyline
        if (room in whole_day_data){
            try{
                socket.emit('customer_data', whole_day_data[room]);
            }catch(e){
                console.log(e.message);
            }
        };
        //sending retailers list for that user to that socket 
        if (room in retailers_visited){
            try{
                socket.emit('retailers_visit_customer',retailers_visited[room]);
            }catch(e){
                console.log(e.message);
            };
        };

    });

    socket.on('leave_room', function(room) {
        //back button pressed , let client leave the room
        console.log("Left room :- " + room);
        socket.leave(room);
    });
    
    
    socket.on('get_global_params',function () {
        socket.emit('global_params',GLOBAL_PARAMS);
        console.log("Sent global params to user");
    })
    
    socket.on('get_customer_list',function () {
        socket.emit('customer_list', latest_data_for_all);
        console.log("Sent customers list to user");
    })
    
    socket.on('get_customer_info',function () {
        socket.emit('customers_info',user_info);
        console.log("Sent customer info to user");
    })

    socket.on('custom_event', function(data) {
        console.log("Custom event");
        console.log(data);

    });
    
    socket.on('user_info',function (data) {
//        console.log(data);
//        console.log(data.length)
//        console.log(data[0].length)
//        console.log(data[1].length)
//        console.log(data[2].length)
        user_info = data;
        console.log(user_info);
        console.log("YES");
    });
    
    socket.on('reset_server_data',function (data) {
        whole_day_data = {};
        latest_data_for_all={};
        movement_in_km = {};
        retailers_visited = {};
        GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0};
    });
    
    socket.on('movement_km',function (data) {
        
        //maintaining distance for each customer
        var customer_id = data[0];
        var distance = data[1];
        if (customer_id in movement_in_km) {
            try{
                movement_in_km[customer_id]=movement_in_km[customer_id]+distance;
                GLOBAL_PARAMS["overall_distance"] = GLOBAL_PARAMS["overall_distance"]+distance;
            }catch(e){
                console.log(e.message);
            }
        }else {
            try{
                movement_in_km[customer_id]=distance;
                GLOBAL_PARAMS["overall_distance"] = GLOBAL_PARAMS["overall_distance"]+distance;
            }catch(e){
                console.log(e.message);
            }
        }
        GLOBAL_PARAMS['distance_individual'] = movement_in_km;
        location_nsp.emit('global_params',GLOBAL_PARAMS);        
    });
    
    socket.on('print_list',function (data) {
        console.log(latest_data_for_all);
    })
    
    
    // socket.on('retailers_visited',function (retailer_visited_list) {
    //     var curr_day = moment().tz("Asia/Kolkata").format("MM-DD-YYYY");

        
    //     for (var i = 0; i < retailer_visited_list.length ; i++) {
    //         data=retailer_visited_list[i];
        
    //         var customer_id = data[0];
    //         var retailer_id = data[1];
    //         var epoch_time = data[2];

    //         var day_of_timestamp = moment.tz(epoch_time,"Asia/Kolkata").format("MM-DD-YYYY")
        
    //         if (curr_day==day_of_timestamp) {
    //             if (customer_id in retailers_visited) {
    //                     if (retailer_id in retailers_visited[customer_id]){
                                    
    //                     }else {
    //                         try{
    //                             retailers_visited[customer_id][retailer_id] = epoch_time;
    //                             GLOBAL_PARAMS["retailers_visited"] = GLOBAL_PARAMS["retailers_visited"] + 1;
    //                             GLOBAL_PARAMS["retailers_individual"] = retailers_visited;
    //                         }catch(e){
    //                             console.log(e.message);
    //                         }
    //                     }
    //             }else {
    //                 try{
    //                     retailers_visited[customer_id] = {};
    //                     retailers_visited[customer_id][retailer_id] = epoch_time;
    //                     GLOBAL_PARAMS["retailers_visited"] = GLOBAL_PARAMS["retailers_visited"] + 1;
    //                     GLOBAL_PARAMS["retailers_individual"] = retailers_visited;
    //                 }catch(e){
    //                     console.log(e.message);
    //                 }
    //             }
    //         }
    //     }
    //     location_nsp.emit('global_params',GLOBAL_PARAMS);
    // });


    socket.on('data', function (data) {
        // body...
        latest_data_for_all = data;
        console.log(latest_data_for_all);
        location_nsp.emit('customer_list', latest_data_for_all);
    });


    // socket.on('data', function(data) {

    //     // parse the string into a list of lists
    //     //console.log('coming here');
	   //  console.log(data);
    //     console.log('Messages received :- ' + data.length);
    //     //iterate over the list 
    //     for (var i = 0; i < data.length; i++) {
    //         var parsedData = data[i];
    //         var epoch_time_of_data = parsedData[1];
    //         var customer_id_of_data = parsedData[0];
    //         //console.log(customer_id_of_data);          

    //             if ((parsedData[4]=='null')||(parsedData[4]=='Unknown')) {
    //                 if(customer_id_of_data in latest_data_for_all){
    //                     try{
    //                         last_data=latest_data_for_all[customer_id_of_data];
    //                         last_data[4]=null;
    //                         last_data[1]=epoch_time_of_data;
    //                         location_nsp.in(customer_id_of_data).emit('latest_data', last_data);
    //                         if(customer_id_of_data in whole_day_data){
    //                             whole_day_data[customer_id_of_data].push(last_data);
    //                         }else{
    //                             whole_day_data[customer_id_of_data] = [last_data];
    //                         }
    //                         latest_data_for_all[customer_id_of_data] = last_data;
    //                     }catch(e){
    //                         console.log(e);
    //                     }
    //                 }
    //             }else {
    //             //first check if customerID from this data is already a key in that whole_day_data structure , if yes then append this data to that customerID list
    //                 if (customer_id_of_data in whole_day_data) {
    //                     try{
    //                         whole_day_data[customer_id_of_data].push(parsedData);
    //                     }catch(e){
    //                         console.log(e.message);
    //                         console.log("CustomerID : " + customer_id_of_data);
    //                         console.log("Data : "+ parsedData);
    //                     }

    //                 } else {
    //                 //if no key present for that customer, make a new key value pair with JSON in the list
    //                     whole_day_data[customer_id_of_data] = [parsedData];
    //                 }

    //                 //keep updating the latest data for each customer
    //                 latest_data_for_all[customer_id_of_data] = parsedData;
    //                 //also send the real-time data to the correct room
    //                 location_nsp.in(customer_id_of_data).emit('latest_data', parsedData);
    //             }

    //     }
    //     //console.log("latest-data" + latest_data_for_all);
    //     // sending data  to each connected client for updating their table
    //     location_nsp.emit('customer_list', latest_data_for_all);

    // });
});


new CronJob('00 00 00 * * *', function() {

    whole_day_data = {};
    movement_in_km = {};
    retailers_visited = {};
    GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0, "distance_individual": movement_in_km, "retailers_individual":retailers_visited};
    whole_day_data = {};
    movement_in_km = {};
    retailers_visited = {};
    GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0, "distance_individual": movement_in_km, "retailers_individual":retailers_visited};

}, null, true, 'Asia/Calcutta');


//used to refresh data every midnight
// ontime({
//     cycle: ['18:30:00'],
//     utc: true
// }, function(ot) {
//     whole_day_data = {};
//     movement_in_km = {};
//     retailers_visited = {};
//     GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0};
//     whole_day_data = {};
//     movement_in_km = {};
//     retailers_visited = {};
//     GLOBAL_PARAMS = { "overall_distance":0 , "retailers_visited":0};
//     ot.done()
//     return
// })



