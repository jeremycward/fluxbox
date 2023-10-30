import React, { FC, useEffect, useState } from "react";
import { useQuery } from '@apollo/client';
import "@react-sigma/core/lib/react-sigma.min.css";
import { parseResults } from "../graphing/graphparser";
import "@react-sigma/core/lib/react-sigma.min.css";
import { SigmaContainer,useRegisterEvents, useSigma  } from "@react-sigma/core";
import {LoadGraph} from './LoadGraph'

import {FLAT_GRAPH_QUERY_ALL} from '../graphing/Queries'




export const DragNdropLoaded: FC = () => {
  const { loading, error, data } = useQuery(FLAT_GRAPH_QUERY_ALL);
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error : {error.message}</p>;

  const GraphEvents: React.FC = () => {
    const registerEvents = useRegisterEvents();
    const sigma = useSigma();
    const [draggedNode, setDraggedNode] = useState<string | null>(null);

    useEffect(() => {
      // Register the events
      registerEvents({
        downNode: (e:any) => {
          setDraggedNode(e.node);
          sigma.getGraph().setNodeAttribute(e.node, "highlighted", true);
        },
        mouseup: (e:any) => {
          if (draggedNode) {
            setDraggedNode(null);
            sigma.getGraph().removeNodeAttribute(draggedNode, "highlighted");
          }
        },
        mousedown: (e:any) => {
          // Disable the autoscale at the first down interaction
          if (!sigma.getCustomBBox()) sigma.setCustomBBox(sigma.getBBox());
        },
        mousemove: (e:any) => {
          if (draggedNode) {
            // Get new position of node
            const pos = sigma.viewportToGraph(e);
            sigma.getGraph().setNodeAttribute(draggedNode, "x", pos.x);
            sigma.getGraph().setNodeAttribute(draggedNode, "y", pos.y);

            // Prevent sigma to move camera:
            e.preventSigmaDefault();
            e.original.preventDefault();
            e.original.stopPropagation();
          }
        },
        touchup: (e:any) => {
          if (draggedNode) {
            setDraggedNode(null);
            sigma.getGraph().removeNodeAttribute(draggedNode, "highlighted");
          }
        },
        touchdown: (e:any) => {
          // Disable the autoscale at the first down interaction
          if (!sigma.getCustomBBox()) sigma.setCustomBBox(sigma.getBBox());
        },
        touchmove: (e:any) => {
          if (draggedNode) {
            // Get new position of node

            // const pos = sigma.viewportToGraph(e);
            // sigma.getGraph().setNodeAttribute(draggedNode, "x", pos.x);
            // sigma.getGraph().setNodeAttribute(draggedNode, "y", pos.y);

            // Prevent sigma to move camera:
            //e.preventSigmaDefault();
            e.original.preventDefault();
            e.original.stopPropagation();
          }
        },
      });
    }, [registerEvents, sigma, draggedNode]);

    return null;
  };

  return (
    <SigmaContainer style={{ height: "1000px", width: "2000px" }}>
      <LoadGraph graph={parseResults(data.flatGraph)} />
      <GraphEvents />
    </SigmaContainer>

  );
};

export default DragNdropLoaded;