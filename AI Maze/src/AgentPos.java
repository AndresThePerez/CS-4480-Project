

public class AgentPos{

    int i,j;                   // cell position
    
    public AgentPos(int i, int j){
     this.i=i;
     this.j=j;
    };
    public int i() { return i;}   // get i (for overloaded methods)

    public int j() { return j;}   // get j (for overloaded methods)
    
    // go up
    public AgentPos north(){
      return new AgentPos(i-1,j);
    }
    
    //go down
    public AgentPos south(){
        return new AgentPos(i+1 , j);
    }
    
    //go right
    public AgentPos east(){
        return new AgentPos(i,j+1);
    }
    
    //go left
    public AgentPos west(){
      return new AgentPos(i,j-1);
    }
    
}
