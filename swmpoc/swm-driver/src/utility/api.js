import axios from 'axios';
import _ from 'lodash';

var instance = axios.create({
  baseURL: "https://gbq2ja2cv8.execute-api.ap-southeast-1.amazonaws.com",
  headers: {
    'Content-Type': 'application/json',
  },
});

//mockdata end point will remove in future
export const commonApi = async(method = "get", context = "", queryObject = {}, body = {}) => {
  var url = context;
  url += !_.isEmpty(queryObject) ? '?' : "";
  for (var variable in queryObject) {
    if (typeof queryObject[variable] !== 'undefined') {
      url += '&' + variable + '=' + queryObject[variable];
    }
  }
  console.log(url);
  switch (method) {
    case "get":
      try {
        return await instance.get(url);
      } catch (e) {
        throw new Error(e);
      }
    case "post":
      try {
        console.log(url);
        return await instance.post(url, body);
      } catch (e) {
        throw new Error(e);
      }
  }
}
