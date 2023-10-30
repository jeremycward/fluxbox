import { useEffect } from "react";
import Graph from "graphology";
import { SigmaContainer, useLoadGraph,useRegisterEvents } from "@react-sigma/core";
import { useQuery, gql } from '@apollo/client';
import "@react-sigma/core/lib/react-sigma.min.css";
import { parseResults } from "../graphing/graphparser";
import {FLAT_GRAPH_QUERY_ALL} from '../graphing/Queries'
import {loadGraph} from './LoadGraph'







export const DisplayGraph = () => {

  const { loading, error, data } = useQuery(FLAT_GRAPH_QUERY_ALL);
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error : {error.message}</p>;
  
  return (
    <SigmaContainer style={{ height: "1500px", width: "2000px"}} settings={{renderEdgeLabels: false }} >      
      <loadGraph graph={parseResults(data.flatGraph)} />
    </SigmaContainer>
  );
};