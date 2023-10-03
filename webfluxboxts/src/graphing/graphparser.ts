import Graph from "graphology";
import {circular} from 'graphology-layout';
export function parseResults(props:any):Graph{
    
    const gr:Graph = new Graph();

    var ctr = 0;
    props.vertices.forEach((vtx:any)=>{
        gr.addNode(vtx.id,{x: 1 , y: 1 ,label: vtx.caption, size:15,color: "#0000FF"})
    })
    props.edges.forEach((edge:any)=>{
         gr.addEdge(edge.from,edge.to,{type:"arrow",size:4})
    })
    const positions = circular(gr);

    circular.assign(gr);


    return gr;



}