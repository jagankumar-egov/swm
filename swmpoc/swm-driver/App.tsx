import 'react-native-get-random-values';
import React from 'react';
import { Alert, Text, TouchableOpacity,View, StyleSheet,Dimensions } from 'react-native'
import Home from './src/components/Home';

import { StatusBar } from 'react-native'




const deviceHeight = Dimensions.get('window').height;
const deviceWidth = Dimensions.get('window').width;



const stylesAlert = StyleSheet.create ({
  button: {
     backgroundColor: '#4ba37b',
     width: 100,
     borderRadius: 50,
     alignItems: 'center',
     marginTop: 100
  }
})

export default function App() {
  return (
    <Home/>
   
  )
  
}

const styles = StyleSheet.create({
  container: {
    
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  webView:{
    flex: 1,
    backgroundColor: 'yellow',
    width: deviceWidth,
    height: deviceHeight
 }
});

