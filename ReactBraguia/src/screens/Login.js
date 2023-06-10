import React, { useEffect, useState } from 'react';
import { View, TextInput, Button, Image, Text, Keyboard, TouchableWithoutFeedback, AsyncStorage } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useSelector, useDispatch } from 'react-redux';
import { setCookies, updateUsername } from '../actions/user';

const LoginActivity = () => {
  const navigation = useNavigation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loginFailed, setLoginFailed] = useState(false);

  const dispatch = useDispatch();

  useEffect(() => {
    const backButtonHandler = () => true;
    navigation.addListener('beforeRemove', (e) => {
      if (!backButtonHandler()) {
        e.preventDefault();
      }
    });
  }, [navigation]);

  const handleLogin = () => {
    validate()
      .catch(() => {
        setLoginFailed(true);
      });
  };

  const validate = () => {
    return new Promise((resolve, reject) => {
      if (username.trim() === '' || password === '') {
        console.log("invalid name");
        reject(new Error('Login failed'));
      } else {
        console.log("valid name");
        makeLoginRequest()
          .then(() => {
            resolve();
          })
          .catch(() => {
            setLoginFailed(true);
            reject(new Error('Login failed'));
          });
      }
    });
  };

  const makeLoginRequest = () => {
    const body = {
      username: username.trim(),
      email: "",
      password: password,
    };
    console.log(body);

    console.log("Started login request");
    return fetch('https://c5a2-193-137-92-29.eu.ngrok.io/login', {
      credentials: 'omit',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    })
      .then(response => {
        if (response.ok) {
          console.log("response ok");
          const cookies = response.headers.map['set-cookie'];
          console.log(cookies)
          const csrfTokenMatch = cookies.match(/csrftoken=([^;]+)/);
          const sessionIdMatch = cookies.match(/sessionid=([^;]+)/);
          const csrfToken = csrfTokenMatch ? csrfTokenMatch[0] : null;
          const sessionId = sessionIdMatch ? sessionIdMatch[0] : null;
          console.log(csrfToken);
          console.log(sessionId);

          if (csrfTokenMatch.length === 2) {
            // Save cookies in Redux store
            console.log("Saved Cookie")
            dispatch(setCookies(csrfToken + ';' + sessionId));
          }
          else {
            throw new Error('Login request failed');
          }
        } else {
          console.log('Login request failed:', response.status);
          throw new Error('Login request failed');
        }
      })
      .catch(error => {
        console.log('Login request failed by error:', error);
        throw new Error('Login request failed');
      });
  };

  const dismissKeyboard = () => {
    Keyboard.dismiss();
  };

  return (
    <TouchableWithoutFeedback onPress={dismissKeyboard}>
      <View style={styles.container}>
        <Image
          source={require('../../assets/logo_red.png')}
          style={styles.image}
        />
        <TextInput
          style={styles.input}
          placeholder="Name"
          value={username}
          onChangeText={setUsername}
        />
        <TextInput
          style={styles.input}
          placeholder="Password"
          secureTextEntry
          value={password}
          onChangeText={setPassword}
        />
        <Button title="Login" onPress={handleLogin} color="#FF0008" />
        {loginFailed && <Text style={styles.loginFailedText} id="login_failed_txt">Login Failed</Text>}
      </View>
    </TouchableWithoutFeedback>
  );
};

const styles = {
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  input: {
    width: '80%',
    height: 40,
    borderWidth: 1,
    borderColor: 'gray',
    marginVertical: 10,
    paddingHorizontal: 10,
  },
  loginFailedText: {
    color: 'red',
    marginTop: 10,
  },
  image: {
    width: 300,
    height: 200,
    marginBottom: 30,
  },
};

export default LoginActivity;
