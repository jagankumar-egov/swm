import axios from 'axios';
import _ from 'lodash';

var instance = axios.create({
  baseURL: window.location.origin,
  headers: {
    'Content-Type': 'application/json',
  },
});
const requestInfo={
    "apiId": "apiId",
    "ver": "version",
    "action": "action",
    "did": "did",
    "key": "key",
    "msgId": "msgId",
    "requesterId": "requestId",
    "authToken": "9b5306f2-b3c5-4d22-b146-bfee7513cffa",
    "userInfo": {
      "type": "SYSTEM",
      "name": "kiran",
      "id": 32
    }
}
//mockdata end point will remove in future
export const commonApi = async(method = "post", context = "/", queryObject = {}, body = {}) => {
  var url = context;
  url += !_.isEmpty(queryObject) ? '?' : "";
  for (var variable in queryObject) {
    if (typeof queryObject[variable] !== 'undefined') {
      url += '&' + variable + '=' + queryObject[variable];
    }
  }
  body={
    requestInfo,
    ...body
  }
  switch (method) {
    case "get":
      try {
        return await instance.get(url);
      } catch (e) {
        throw new Error(e);
      }
    case "post":
      try {
        return await instance.post(
          url,
          body
        );
      } catch (e) {
        throw new Error(e);
      }
  }
}
