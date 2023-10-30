import { DisplayGraph} from './components/sigmasimple'

import { ApolloClient, ApolloProvider, InMemoryCache } from '@apollo/client';
import { FLAT_GRAPH_QUERY_ALL } from './graphing/Queries';
import { MockedProvider } from "@apollo/client/testing";
import Sigma from "sigma";
import Events from './components/SimpleSigma';
import {DragNdropLoaded} from './components/DndSigma'
const client = new ApolloClient({
  uri: "http://localhost:8080/graphql",
  cache: new InMemoryCache(),
});



const mocks:any = [
  {
    request: {
      query: FLAT_GRAPH_QUERY_ALL,
      variables: {}
    },
    result: {
      "data": {
        "flatGraph": {
          "vertices": [
            {
              "name": "name_1",
              "caption": "caption_1",
              "attributes": [
                {
                  "name": "matrix_x_pos",
                  "value": "0"
                },
                {
                  "name": "matrix_y_pos",
                  "value": "0"
                },
                {
                  "name": "cellType",
                  "value": "InputCell"
                }
              ],
              "id": "id_1"
            },
            {
              "name": "name_2",
              "caption": "caption_2",
              "attributes": [
                {
                  "name": "matrix_x_pos",
                  "value": "0"
                },
                {
                  "name": "matrix_y_pos",
                  "value": "1"
                },
                {
                  "name": "cellType",
                  "value": "MktDataCell"
                }
              ],
              "id": "id_2"
            }
           
          
          ],
          "edges": [
            {
              "name": "edge_1_name",
              "caption": "edge_1_caption",
              "from": "id_1",
              "to": "id_2",
              "id": "id_edge_1"
            }
   
          ]
        }
      }
    }
  }

]; 



function App() {
  return (


<ApolloProvider client={client}>
            {/* <DisplayGraph></DisplayGraph> */}
            <DragNdropLoaded></DragNdropLoaded>

    </ApolloProvider>

      

  );
}

export default App;
