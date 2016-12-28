import java.io.*;
import java.util.ArrayList;


public class DenseMat implements Matrix {

    public DenseMat(String fileName) throws IOException {
        if (fileName != null) {
            this.list = new ArrayList();
            this.list = this.getMat(fileName);
            this.arr = this.toArray(this.list);
            this.list.clear();
            this.list = null;
        }
    }

    @Override
    public Matrix mul(Matrix b) throws IOException, InterruptedException {
        DenseMat resD;
        DenseMat a = this;
        resD = a.mulDD((DenseMat) b);
        return resD;
    }

    @Override
    public Matrix pmul(Matrix b) throws IOException, InterruptedException {
        DenseMat resD;
        DenseMat a = this;
        resD = a.pmulDD((DenseMat) b);
        return resD;
    }

    @Override
    public void save(String fileName) throws IOException {
        DenseMat c = this;
        PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
        for (int i = 0; i < c.arr.length; i++) {
            for (int j = 0; j < c.arr.length; j++) {
                printWriter.print(c.arr[i][j] + " ");
            }
            printWriter.println();
        }
        printWriter.close();
    }


    static BufferedReader r;
    static ArrayList<ArrayList> arrayList;
    static ArrayList<Double> row;
    private String fileName;
    public double[][] arr;
    public ArrayList list;

    private DenseMat mulDD(DenseMat b) throws IOException {
        DenseMat a = this;
        DenseMat res = new DenseMat(null);
        b = b.transpose(b);
        res.arr = new double[a.arr.length][a.arr.length];
        for (int i = 0; i < a.arr.length; i++) {
            for (int j = 0; j < a.arr.length; j++) {
                for (int k = 0; k < a.arr.length; k++) {
                    res.arr[i][j] = res.arr[i][j] + a.arr[i][k] * b.arr[j][k];
                }
            }
        }
        return res;
    }

    private DenseMat pmulDD(DenseMat m2) throws InterruptedException, IOException {
        DenseMat m1 = this;
        DenseMat res = new DenseMat(null);
        m2 = m2.transpose(m2);
        res.arr = new double[m1.arr.length][m1.arr.length];
        Dispatch d = new Dispatch();
        DenseMat finalM = m2;
        class MyCode implements Runnable {
            public void run() {
                for (int i = d.next(); i < m1.arr.length; i = d.next()) {


                    for (int j = 0; j < finalM.arr.length; j++) {
                        for (int k = 0; k < finalM.arr.length; k++) {
                            res.arr[i][j] = res.arr[i][j] + m1.arr[i][k] * finalM.arr[j][k];
                        }
                    }
                }
            }

        }

        int core_count = Runtime.getRuntime().availableProcessors();
        Thread[] t = new Thread[core_count];
        for (int i = 0; i < core_count; i++) {
            Thread t1 = new Thread(new MyCode());
            t[i] = t1;
        }
        for (int i = 0; i < core_count; i++) {
            t[i].start();
        }
        for (int i = 0; i < core_count; i++) {
            t[i].join();
        }
        return res;
    }

    private DenseMat transpose(DenseMat a) throws IOException {
        DenseMat newA = new DenseMat(null);
        newA.arr = new double[a.arr.length][a.arr.length];
        for (int i = 0; i < a.arr.length; i++) {
            for (int j = 0; j < a.arr.length; j++) {
                newA.arr[i][j] = a.arr[j][i];
            }
        }
        return newA;
    }

    public static void printf(DenseMat a) {
        for (int i = 0; i < a.arr.length; i++) {
            for (int j = 0; j < a.arr.length; j++) {
                System.out.print(a.arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void openFile(String fileName) {
        try {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        } catch (Exception e) {
            System.out.println("not found");
        }
    }

    private void readFile() throws IOException {

        String s = r.readLine();

        while (s != null) {
            for (String val : s.split(" ")) {
                row.add(Double.parseDouble(val));
            }
            arrayList.add((ArrayList) row.clone());
            row.clear();
            s = r.readLine();
        }

    }

    public ArrayList<ArrayList> getMat(String fileName) throws IOException {
        arrayList = new ArrayList<ArrayList>();
        row = new ArrayList<Double>();
        this.fileName = fileName;
        openFile(fileName);
        readFile();
        return arrayList;
    }

    private double[][] toArray(ArrayList<ArrayList> arrayList) {
        double[][] res = new double[arrayList.size()][arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                res[i][j] = (double) arrayList.get(i).get(j);
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object bm) {
        boolean ans;
        if (bm instanceof DenseMat) {
            boolean ans1 = true;
            DenseMat a = this;
            DenseMat b = (DenseMat) bm;
            for (int i = 0; i < a.arr.length; i++) {
                for (int j = 0; j < b.arr.length; j++) {
                    if (a.arr[i][j] != b.arr[i][j]) ans1 = false;
                }
            }
            ans = ans1;
        } else ans = false;
        return ans;
    }
}