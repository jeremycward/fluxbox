import {LoadGraph, DisplayGraph} from './components/sigmasimple'
import { ApolloClient, ApolloProvider, InMemoryCache } from '@apollo/client';
const client = new ApolloClient({
  uri: "http://localhost:8080/graphql",
  cache: new InMemoryCache(),
});

function App() {
  return (
    <ApolloProvider client={client}>
      <DisplayGraph></DisplayGraph>
    </ApolloProvider>
      

  );
}

export default App;
