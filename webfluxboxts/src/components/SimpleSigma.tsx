import React, { FC, useEffect } from "react";
import { parseResults } from "../graphing/graphparser";
import "@react-sigma/core/lib/react-sigma.min.css";
import { SigmaContainer } from "@react-sigma/core";
import {LoadGraph} from './LoadGraph'
import { useQuery, gql } from '@apollo/client';
import "@react-sigma/core/lib/react-sigma.min.css";
import {FLAT_GRAPH_QUERY_ALL} from '../graphing/Queries'




const Events: FC = () => {
   

    const { loading, error, data } = useQuery(FLAT_GRAPH_QUERY_ALL);
    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error : {error.message}</p>;

  return (
    <SigmaContainer style={{ height: "500px" }}>
       <LoadGraph graph={parseResults(data.flatGraph)} />
       </SigmaContainer>

  )
  }
export default Events;