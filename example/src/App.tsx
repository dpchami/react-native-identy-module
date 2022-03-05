import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { multiply, getFingerprint } from 'react-native-identy-module';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);

  const startIdenty = () => {
    getFingerprint()
    .then(res => {
      console.log(res);
    })
    .catch(err => {
      console.log(err);
    });
}

  return (
    <View style={styles.container}>
      <Text style={{ fontSize:16, marginBottom:20 }}>Start identy SDK</Text>
          <Button
              onPress={startIdenty}
              title="Start Identy"
              color="#841584"
          />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
