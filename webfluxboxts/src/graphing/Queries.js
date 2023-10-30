
import { gql } from '@apollo/client';
export  const FLAT_GRAPH_QUERY_ALL = gql `
{
  flatGraph {
    vertices {
      name
      caption
      attributes{
        name 
        value#
      }
      id
    }
    edges {
      name
      caption
      from
      to
      id
    }
  }
}
`