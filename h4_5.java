import java.util.Arrays;
import java.util.Random;

/**
 * @author huyue
 * @date 2018/10/31-19:39
 */
public class h4_5 {
    public static void main(String[] args) {
        Sudoku sudoku=new Sudoku();
        sudoku.init();
        System.out.println("随机生成棋盘：");
        sudoku.printSudo();
        System.out.println("挖空后：");
        sudoku.dig();
        sudoku.printSudo();
        System.out.println("求解后：");
        sudoku.solve();
        sudoku.printSudo();
    }
}

class Sudoku{
    private int[][] sudo=new int[9][9];
    private boolean[][][] sudoCheck=new boolean[9][9][9];
    private Random random=new Random(System.currentTimeMillis());

    public Sudoku(){
        for (int i=0;i<sudo.length;i++){
            for (int j=0;j<sudo[i].length;j++){
                sudo[i][j]=0;
            }
        }
        for (int i=0;i<sudoCheck.length;i++){
            for (int j=0;j<sudoCheck[i].length;j++){
                for (int t=0;t<sudoCheck[i][j].length;t++){
                    sudoCheck[i][j][t]=false;
                }
            }
        }
    }
    private boolean check(int i,int j){
        for (int a=0;a<9;a++){
            if (a==j) continue;
            if (sudo[i][j]==sudo[i][a]) return false;
        }
        for (int b=0;b<9;b++){
            if (b==i) continue;
            if (sudo[i][j]==sudo[b][j]) return false;
        }
        int row=i/3;
        int col=j/3;
        for (int a=0;a<3;a++){
            for (int b=0;b<3;b++){
                if (i==row*3+a || j==col*3+b) continue;
                if (sudo[i][j]==sudo[row*3+a][col*3+b]) return false;
            }
        }
        return true;
    }
    private boolean insert(int i,int j,int num){
        int temp=sudo[i][j];
        sudo[i][j]=num;
        if (check(i,j)){
            sudoCheck[i][j][num-1]=true;
            return true;
        }else {
            sudo[i][j]=temp;
            sudoCheck[i][j][num-1]=true;
            return false;
        }
    }
    private int getNext(int i,int j){
        int fin;
        int size=0;
        for(int a=0;a<j;a++){
            sudoCheck[i][j][sudo[i][a]-1]=true;
        }
        for (int a=0;a<9;a++){
            if (!sudoCheck[i][j][a]){
                size+=1;
            }
        }
        if (size==0) return 0;
        int c=random.nextInt(size)+1;
        int point=0;
        for (int a=0;a<c;){
            if (!sudoCheck[i][j][point]){
                a++;
            }
            point++;
        }
        fin=point;
        return fin;
    }
    private boolean set(int i,int j){
        int next=getNext(i,j);
        while (next>0){
            if (insert(i,j,(char) next)){
                return true;
            }
            next=getNext(i,j);
        }
        Arrays.fill(sudoCheck[i][j],false);
        return false;
    }
    public void init(){
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (!set(i,j)){
                    if (j==0 && i!=0){
                        i--;
                        j=7;
                    }else {
                        j-=2;
                    }
                }
            }
        }
    }
    public void printSudo(){
        for (int i=0;i<9;i++){
            System.out.println(Arrays.toString(sudo[i]));
        }
    }
    private int[] getPoint(){
        int[] f=new int[2];
        do{
            f[0]=random.nextInt(9);
            f[1]=random.nextInt(9);
        }while (sudo[f[0]][f[1]]==0);
        return f;
    }
    private boolean canDig(int i,int j){
        int temp=sudo[i][j];
        int[][] tem=new int[9][9];
        for (int a=0;a<9;a++){
            for (int b=0;b<9;b++){
                tem[a][b]=sudo[a][b];
            }
        }
        for (int a=1;a<10;a++){
            if (a==temp) continue;
            if (insert(i,j,a)){
                if (solve()){
                    for (int x=0;x<9;x++){
                        for (int b=0;b<9;b++){
                            sudo[x][b]=tem[x][b];
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    public void dig(){
        for (int i=0;i<20;i++){
            int[] point=getPoint();
            if (canDig(point[0],point[1])){
                sudo[point[0]][point[1]]=0;
            }else{
                i--;
            }
        }
    }
    public boolean solve(){
        int[][] map=new int[47][2];
        int point=0;
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (sudo[i][j]>0) continue;
                map[point][0]=i;
                map[point][1]=j;
                point++;
            }
        }
        int size=point;
        for (int a=0;a<size;a++){
            int b;
            for (b=sudo[map[a][0]][map[a][1]]+1;b<10;b++){
                if (insert(map[a][0],map[a][1],b)){
                    break;
                }
            }
            if (b==10){
                sudo[map[a][0]][map[a][1]]=0;
                if (a==0){
                    return false;
                }else {
                    a-=2;
                }
            }
        }
        return true;
    }
}