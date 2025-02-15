import React, { useEffect, useState, useContext} from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Linking } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useSelector } from 'react-redux';

import themeContext from '../theme/themeContext';

const Socials = () => {
  const [socials, setSocials] = useState([]);

  const theme = useContext(themeContext)

  const initialData = useSelector((state) => state.appData.appinfo);
  useEffect(() => {
    if (initialData) setSocials(initialData.socials);
  }, []);

  return (
    <View style={[styles.container, {backgroundColor:theme.backgroundColor}]}>
      <View style={styles.content}>
        <View style={styles.socialInfo}>
          {socials.map((item) => (
            <TouchableOpacity
              key={item.social_name}
              style={styles.socialDetail}
            >
              <Ionicons name="logo-facebook" size={50} color="blue" />
              <View style={styles.socialDetailsContainer}>
                <Text style={[styles.socialName, {color:theme.color}]}>{item.social_name}</Text>
                {item.social_url && (
                  <TouchableOpacity onPress={() => Linking.openURL(item.social_url)}>
                    <Text style={[styles.socialUrl, {color:theme.color}]} selectable={false}>{item.social_url}</Text>
                  </TouchableOpacity>
                )}
              </View>
            </TouchableOpacity>
          ))}
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  socialInfo: {
    alignItems: 'center',
  },
  socialDetail: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  socialDetailsContainer: {
    marginLeft: 10,
  },
  socialName: {
    fontSize: 25,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  socialUrl: {
    fontSize: 25,
    textDecorationLine: 'underline',
  },
});

export default Socials;
//TODO: CORRIGIR!