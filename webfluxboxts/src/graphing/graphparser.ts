import Graph from "graphology";
import forceAtlas2 from 'graphology-layout-forceatlas2';
import { DirectedGraph } from "graphology";


const colorTypeMap = new Map([
    ["MktDataCell" , "#FF00FF"],
    ["SegmentCell" , "#00FF00"],
    ["InputCell" , "#000000"],
    ["OutputCell" , "#000000"],
    ["CurveCell" , "#0000FF"]


])


export function parseResults(props: any): Graph {
    const gr: Graph = new DirectedGraph();

    var ctr = 0;
    props.vertices.forEach((vtx: any) => {
        const attrMap: any = new Map(vtx.attributes.map((el: any) => [el.name, el.value]));
        const xPos: Number = parseInt(attrMap.get("matrix_x_pos")) 
        const yPos: Number = parseInt(attrMap.get("matrix_y_pos"))
        const cellType:string = attrMap.get("cellType")
        const cellTypeColor= colorTypeMap.get(cellType)


        gr.addNode(vtx.id, { x: xPos, y: yPos , label: vtx.caption, size: 10, color: cellTypeColor })
    })
    props.edges.forEach((edge: any) => {
        gr.addEdge(edge.from, edge.to, {
            type: "arrow", label: 'feeds',color: "#000000",
            size: 1
        })
    })

    forceAtlas2.assign(gr,{
        iterations: 200,
        settings: {
          gravity: 1

        }
      });

    return gr;

}








