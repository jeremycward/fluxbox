import { useEffect } from "react";

import { useLoadGraph} from "@react-sigma/core";

import "@react-sigma/core/lib/react-sigma.min.css";

import {FLAT_GRAPH_QUERY_ALL} from '../graphing/Queries'


export const LoadGraph = (props:any) => {
    const loadGraph = useLoadGraph();
    useEffect(() => {
  
      loadGraph(props.graph);
    }, [loadGraph]);
  
    return null;
  };