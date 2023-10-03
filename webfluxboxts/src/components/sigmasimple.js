import { useEffect } from "react";
import Graph from "graphology";
import { SigmaContainer, useLoadGraph } from "@react-sigma/core";
import { useQuery, gql } from '@apollo/client';
import "@react-sigma/core/lib/react-sigma.min.css";
import { parseResults } from "../graphing/graphparser";


const Q = gql `
{
  flatGraph {
    vertices {
      name
      caption
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



export const LoadGraph = (props) => {
  const loadGraph = useLoadGraph();

  useEffect(() => {

    loadGraph(props.graph);
  }, [loadGraph]);

  return null;
};

export const DisplayGraph = () => {

  const { loading, error, data } = useQuery(Q);
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error : {error.message}</p>;
  console.log(data)
  return (
    <SigmaContainer style={{ height: "500px", width: "1500px" }}>
      
      <LoadGraph graph={parseResults(data.flatGraph)} />
    </SigmaContainer>
  );
};