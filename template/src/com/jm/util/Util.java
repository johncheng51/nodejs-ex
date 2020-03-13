package com.jm.util;


import com.jm.xml.ReadHash;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Point;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Util {

    public static String[] getSArray(Collection c) {
        String[] sa = new String[c.size()];
        c.toArray(sa);
        Arrays.sort(sa);
        return sa;
    }

    public static PrintWriter getPrinter(String output) {
        try {
            FileOutputStream fos = new FileOutputStream(output);
            PrintWriter pw = new PrintWriter(fos);
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String[] getArray(Collection c) {
        String[] sa = new String[c.size()];
        c.toArray(sa);
        return sa;
    }

    public static String[] getClassFields(String className) {
        Vector v = new Vector();
        try {
            Class c = Class.forName(className);
            Method[] ma = c.getMethods();
            for (Method m : ma) {
                String name = m.getName();
                if (!name.startsWith("get"))
                    continue;
                name = name.substring(3);
                if (name.equals("Class"))
                    continue;
                v.add(name);
            }
            String[] result = new String[v.size()];
            v.toArray(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getObjectValues(Object o) {
        String[] names = getClassFields(o.getClass().getName());
        String[] result = new String[names.length];
        int count = 0;
        for (String name : names)
            result[count++] = "" + getProperty(o, name);
        return result;
    }

    public static Hashtable readHash(String s) {
        ReadHash rh = new ReadHash(s);
        return rh.getTable();
    }

    public static File writeFile(File file, String data) {
        return writeFile(file.getAbsolutePath(), data.getBytes());
    }

    public static File writeFile(File file, byte[] data) {
        return writeFile(file.getAbsolutePath(), data);
    }

    public static File writeFile(String fileName, byte[] data) {
        makeFolder(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.close();
            return new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    private static String replaceUnder(String text){
        String result="";
        char[] ca=text.toCharArray();
        for (char c:ca){
            result+= c=='/'? '\\':c; 
        }
        return result;
    }

    public static void makeFolder(String fileName) {
        fileName=replaceUnder(fileName);
        String newFile = "";
        String[] sa = split(fileName, "\\");
        for (String s : sa) {
            if (s.indexOf(".") >= 0)  break;
            newFile += s + "\\";
            File f = new File(newFile);
            if (!f.exists())
                f.mkdir();
        }
    }

    public static void writeFile(String folder, String file, String data) {
        makeFolder(folder);
        File myFile = new File(folder, file);
        log("Write File:" + myFile);
        writeFile(myFile, data);
    }


    public static String[] copyTo(Object[] source, int n) {
        int len = source.length - n;
        String[] result = new String[len];
        System.arraycopy(source, n, result, 0, len);
        return result;
    }

    public static String getElem(String s, String pat, int n) {
        String[] sa = split(s, pat);
        if (n < sa.length)
            return sa[n];
        else
            return sa[0];
    }

    public static String getElemWithNull(String s, String pat, int n) {
        String[] sa = split(s, pat);
        if (n < sa.length)
            return sa[n];
        else
            return null;
    }

    public static Hashtable stringToHash(String s) {
        return (Hashtable) stringToObject(s);
    }

    public static Object stringToObject(String s) {
        try {
            Base64 base = new Base64();
            byte[] ba = base.decode(s);
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hashToString(Hashtable table) {
        return objectToString(table);
    }

    public static String objectToString(Object table) {
        try {
            Base64 base = new Base64();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ois = new ObjectOutputStream(baos);
            ois.writeObject(table);
            return base.encode(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Hashtable readHashh(String ss) {
        Hashtable<String, String> table = new Hashtable();
        String data = ss;
        while (true) {
            int n = data.indexOf("=");
            if (n == -1)
                return table;
            String key = data.substring(0, n).trim();
            int n1 = data.indexOf("=", n + 1);
            if (n1 == -1) {
                String value = data.substring(n + 1).trim();
                table.put(key, value);
                return table;
            }
            n1 = findWordB(data, n1 - 1);
            String value = data.substring(n + 1, n1).trim();
            table.put(key, value);
            data = data.substring(n1);
        }
    }

    private static int findWordB(String s, int n1) {
        boolean bespace = s.charAt(n1) == ' ';
        boolean oldspace = false;
        for (int count = 0, i = n1; i >= 0; i--) {
            boolean space = s.charAt(i) == ' ';
            boolean change = space && !oldspace;
            if (change)
                count++;
            oldspace = space;
            if (bespace && count == 2)
                return i;
            else if (count == 1)
                return i;
        }
        return -1;
    }


    private static boolean debug = true;

    public static void setDebug(boolean flag) {
        debug = flag;
    }

    public static boolean debug() {
        return debug;
    }

    public static String[] split(String s, String patt) {
        StringTokenizer st = new StringTokenizer(s, patt);
        String sa[] = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreElements())
            sa[i++] = ((String) st.nextElement()).trim();
        return sa;
    }

    public static ArrayList<String> splitPatt(String s, String patt) {
        ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        int p = 0;
        int n = patt.length();
        while (s.indexOf(patt) > 0) {
            p = s.indexOf(patt);
            if (p > 0)
                list.add(s.substring(0, p).trim());
            s = s.substring(p + n).trim();
            i++;
        }
        if (s.length() > 0)
            list.add(s.trim());
        return list;
    }

    public static char findCommand(String table, String pattern) {
        if (pattern.length() < 2)
            return 0;
        int index = table.indexOf(" " + pattern);
        if (index == -1)
            return 0;
        index -= 1;
        return table.charAt(index);
    }

    public static String getField(Object o, String field) {
        Field f = null;
        try {
            f = o.getClass().getField(field);
            return (String) f.get(o);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setField(Object o, String field, String val) {
        Field f = null;
        try {
            f = o.getClass().getField(field);
            f.set(o, val);
        } catch (Exception e) {
        }
    }

    public static boolean isClass(String s) {
        return Character.isUpperCase(s.charAt(0));
    }

    public static Object getProperty(Object bean, String field) {
        Object result = null;
        Class sClass = bean.getClass();
        String cname = cap(true, field);
        try {
            Method m = sClass.getMethod(cname, new Class[] { });
            result = m.invoke(bean, new Object[] { });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean setProperty(Object bean, String field, Object value) {
        Class sClass = bean.getClass();
        String cname = cap(false, field);
        try {
            Method m = sClass.getMethod(cname, new Class[] { value.getClass() });
            m.invoke(bean, new Object[] { value });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getException(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String result = sw + "";
        return result;
    }

    public static String getReason(Exception e) {
        String expt = getException(e);
        int n = expt.indexOf("Caused by:");
        if (n < 0)
            return expt;
        int n1 = expt.indexOf("#####");
        int count = n1 < 0 ? n + 300 : n1;
        return expt.substring(n, count) + "\n";
    }

    public static void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (Exception e) {
        }
    }

    public static void uwait(Class c) {
        try {
            c.wait();
        } catch (Exception e) {
        }
    }

    public static String cap(boolean isGet, String s) {
        String verb = isGet ? "get" : "set";
        String ret = verb + s.substring(0, 1).toUpperCase() + s.substring(1);
        return ret;
    }

    public static String cap(String s) {
        String ret = s.substring(0, 1).toUpperCase() + s.substring(1);
        return ret;
    }

    public static String catchException(String msg) {
        String error = "";
        try {
            throw new Exception();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            error = "\n" + msg;
            e.printStackTrace(new PrintWriter(sw));
            error += sw.toString();
            if (error.length() > 1000)
                error = error.substring(0, 1000);
            return error;
        }
    }


    public static String removeLast(String s, int n) {
        if (s == null || s.length() == 0)
            return s;
        return s.substring(0, s.length() - n);
    }

    public static String removeLast(String s) {
        return removeLast(s, 1);
    }


    static PrintWriter pw = null;

    public static void log(String msg) {
        System.out.println("Util:" + msg);
    }

    public static void logx(String msg) {

        if (pw == null) {
            try {
                pw = new PrintWriter(new FileOutputStream("C:\\test.log", true));
            } catch (Exception e) {
            }
        }
        pw.println(msg);
        pw.flush();
        pw.close();
        //pw=null;
    }

    public static boolean isTrue(String s) {
        if (s == null)
            return false;
        return s.equalsIgnoreCase("Y") || s.equalsIgnoreCase("YES") || s.equalsIgnoreCase("TRUE");
    }


    public static String catchErr(String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append("ERROR message=" + msg + "\n");
        try {
            throw new Exception();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sb.append(sw.toString());
            return sb + "";
        }
    }


    public static String getPrefix(String s, int n, String patt) {
        String result = "";
        String data = s;
        for (int i = 0; i < n; i++) {
            int nn = data.indexOf(patt);
            if (nn < 0)
                return result;
            result += data.substring(0, nn + patt.length());
            data = data.substring(nn + patt.length());
        }
        result = removeLast(result, 1);
        return result;
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0 || s.trim().toLowerCase().equals("null");
    }

    public static String getWord(String s) {
        if (s == null || s.trim().length() == 0)
            return "";
        String[] sa = split(s, " ");
        return sa[0];
    }

    public static String[] splitLast(String s) {
        return splitLast(s, " ");
    }


    public static String[] splitLast(String s, String pattern) {
        String[] sa = split(s, pattern);
        String ss = "";
        for (int i = 0; i < sa.length - 1; i++)
            ss += sa[i] + pattern;
        return new String[] { ss, sa[sa.length - 1] };
    }

    public static String[][] getDummyArray(int x) {
        String[][] newdata = new String[200][x];
        for (int i = 0; i < newdata.length; i++)
            for (int j = 0; j < newdata[0].length; j++)
                newdata[i][j] = "";
        return newdata;
    }


    public static String getFileContent(InputStream is) {
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(is);
            while ((line = dis.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
        }
        return sb + "";
    }

    public static String getFileContent(File file) {
        InputStream is;
        try {
            is = new FileInputStream(file);
            return getFileContent(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getLines(File file) {
        String text = getFileContent(file);
        return getLines(text);
    }

    public static String getContent(File f) {
        try {
            return getContent(new FileInputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getContent(InputStream is) {
        try {
            BufferedReader dis = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String inputLine = null;
            while ((inputLine = dis.readLine()) != null)
                sb.append(inputLine + "\r\n");
            return sb + "";
        } catch (Exception e) {
            return null;
        }
    }

    public static String removeLastWord(String s) {
        int n = s.lastIndexOf(".");
        String result = s.substring(0, n);
        return result;
    }

    public static String removeLastWord(String s, String patt) {
        int n = s.lastIndexOf(patt);
        String result = s.substring(0, n);
        return result;
    }

    public static String removeFirst(String s) {
        int n = s.indexOf(".");
        String result = s.substring(n);
        return result;
    }


    public static String getPrefix(String s) {
        String result = "";
        String[] sa = split(s, ".");
        for (int i = 0; i < (sa.length - 1); i++)
            result += sa[i] + ".";
        return removeLast(result, 1);
    }


    public static String getLast(String s, String pattern) {
        String[] sa = split(s, pattern);
        return sa[sa.length - 1];
    }

    public static String getFirst(String text, String pattern) {
        String[] sa = split(text, pattern);
        return sa[0];
    }

    public static String getSecond(String s, String pattern) {
        String[] sa = split(s, pattern);
        return sa.length == 1 ? sa[0] : sa[1];
    }

    public static String getLeft(String s, String pattern) {
        String last = getLast(s, pattern);
        int n = s.indexOf(last);
        String rest = s.substring(0, n);
        return rest;
    }

    public static String getRight(String s, String pattern) {
        int n = s.indexOf(pattern);
        if (n == -1)
            return s;
        String rest = s.substring(n + 1);
        return rest;
    }

    public static char findKey(Hashtable<String, String> table, String match) {
        for (String key : table.keySet()) {
            if (!match.startsWith(key))
                continue;
            char c = table.get(key).charAt(0);
            return c;
        }
        return 0;
    }

    public static String readUrlText(String url){
        String text=new String(readUrl(url));
        return text;
    }
    public static byte[] readUrl(String s) {
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream is = conn.getInputStream();
            int n = conn.getContentLength();
            byte[] result = null;
            if (n == -1)
                result = getContent(is).getBytes();
            else {
                result = new byte[n];
                BufferedInputStream bis = (new BufferedInputStream(is));
                DataInputStream dis = new DataInputStream(bis);
                dis.readFully(result);
            }
            conn.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFileByte(String file) {
        try {
            InputStream is = new FileInputStream(file);
            return readBytes(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readBytes(InputStream is) {
        try {
            BufferedInputStream bis = (new BufferedInputStream(is));
            DataInputStream dis = new DataInputStream(bis);
            int n = dis.available();
            byte[] result = new byte[n];
            dis.readFully(result);
            dis.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readTcp(InputStream is) {
        try {
            StringBuffer sb = new StringBuffer();
            DataInputStream dis = new DataInputStream(is);
            String line;
            while ((line = dis.readLine()) != null)
                sb.append(line + "\n");
            return sb + "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String readFile(File file,boolean print) {
        return readFile(file.getAbsolutePath(),print);
    }

    public static String readFile(String file,boolean print) {
        File xFile=new File(file);
        if (!xFile.exists()) {
            if (!file.contains("html.txt") && print)
             log("========>"+file+ " is not Exist");
            return null;
        }
        try {
            InputStream is = new FileInputStream(file);
            byte[] result = readBytes(is);
            is.close();
            return new String(result);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }


    public static void saveFile(String file, String data) {
        makeFolder(file);
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static File[] getSelectFiles(File folder, String pat) {
        final String[] sa = Util.split(pat, "|");
        FileFilter ffjar = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (pathname.isDirectory())
                    return false;
                for (int i = 0; i < sa.length; i++)
                    if (name.endsWith("." + sa[i]))
                        return true;
                return false;
            }
        };
        File[] files = folder.listFiles(ffjar);
        return files;
    }

    public static String[] getLines(String s) {
        Vector<String> v = new Vector();
        Scanner scan = new Scanner(s);
        while (scan.hasNextLine())
            v.add(scan.nextLine() + "\n");
        String[] result = new String[v.size()];
        v.copyInto(result);
        return result;
    }

    public static String[] getLines(String s, String pattern) {
        Vector<String> v = new Vector();
        Scanner scan = new Scanner(s);
        while (scan.hasNextLine())
            v.add(scan.nextLine());
        String[] result = new String[v.size()];
        v.copyInto(result);
        return result;
    }

    public static String removeRTN(String s) {
        String result = "";
        String[] sa = getLines(s);
        for (String ss : sa)
            result += ss.trim();
        return result;
    }

    public static String printf(String format, Object[] o) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf(format, o);
        return sw + "";
    }

    public static String print1f(String format, String arg) {
        return printf(format, new String[] { arg });
    }

    public static String print1fs(String format, String arg) {
        format = replaceAll(format, '|', '"');
        return print1f(format, arg);
    }


    public static double getMax(double[] x) {
        double max = x[0];
        for (int i = 1; i < x.length; i++)
            if (x[i] > max)
                max = x[i];
        return max;
    }

    /*
    *  find Max value of vector x
    */
    public static double getMin(double[] x) {
        double min = x[0];
        for (int i = 1; i < x.length; i++)
            if (x[i] < min)
                min = x[i];
        return min;
    }


    public static double[] delta(double[] aa) {
        double[] newresult = new double[aa.length - 4];
        for (int i = 2; i < aa.length - 2; i++)
            newresult[i - 2] = Math.abs(aa[i + 2] + aa[i + 1] - aa[i - 1] - aa[i - 2]);
        return getMMax(newresult);
    }

    public static double[] getMMax(double[] aa) {
        Vector<Double> v = new Vector();
        double current;
        for (int i = 1; i < aa.length - 1; i++) {
            current = aa[i];
            if (current > aa[i - 1] && current > aa[i + 1])
                v.add(current);
        }
        double[] result = new double[v.size()];
        for (int i = 0; i < v.size(); i++)
            result[i] = v.get(i);
        Arrays.sort(result);
        double[] newresult = new double[5];
        for (int count = 0, i = result.length - 1; i >= 0; count++, i--) {
            newresult[count] = result[i];
            if (count == 4)
                break;
        }
        return normData(newresult);
    }

    /**
     * normalize the double
     *
     * @param a
     * @return
     */
    public static double[] norm(double[] aa) {
        int SIZE = 10, oldstep = -1, step = 0;
        boolean change;
        double[] result = new double[SIZE];
        double ratio = SIZE;
        double[] a = norm0(aa);
        ratio /= a.length;
        for (int i = 0; i < a.length; i++) {
            step = (int) (i * ratio);
            if (i == 0)
                oldstep = step;
            double res = (i * ratio) - step;
            change = step != oldstep;
            if (!change)
                result[step] += a[i];
            else {
                result[step - 1] += a[i] * (ratio - res) / ratio;
                result[step] += a[i] * res / ratio;
            }
            oldstep = step;
        }
        return normData(result);
    }

    public static double[] normData(double[] data) {
        double sum = 0;
        double len = data.length;
        for (int i = 0; i < data.length; i++)
            sum += data[i];
        if (sum == 0)
            sum = 1;
        for (int i = 0; i < data.length; i++)
            data[i] *= len / sum;
        return data;
    }

    public static double[] norm0(double[] aa) {
        int begin = 0, end = 0;
        for (int i = 0; i < aa.length; i++)
            if (aa[i] > 0) {
                begin = i;
                break;
            }
        for (int i = aa.length - 1; i > 0; i--)
            if (aa[i] > 0) {
                end = i;
                break;
            }
        double[] result = new double[end - begin + 1];
        for (int i = begin; i < end + 1; i++)
            result[i - begin] = aa[i];
        return result;
    }

    public static Object[] getInts(String s) {
        String[] sa = split(s, "_");
        int[] result = new int[sa.length];
        for (int i = 0; i < sa.length; i++) {
            try {
                result[i] = Integer.parseInt(sa[i]);
            } catch (Exception e) {
            }
        }
        return new Object[] { result, sa };
    }

    public static double length(double[] x) {
        return getMax(x) - getMin(x);
    }

    public static Point getMin(Point[] pa) {
        Comparator c = new Comparator() {
            public int compare(Object o1, Object o2) {
                Point p1 = (Point) o1;
                Point p2 = (Point) o2;
                int delta = (p1.x - p2.x) + (p1.y - p2.y);
                return (new Integer(delta)).compareTo(new Integer(0));
            }
        };
        Arrays.sort(pa, c);
        return pa[0];
    }

    public static String[] splitq(String s, String pattern) {
        String[] sa = Util.split(s, pattern);
        for (int i = 0; i < sa.length; i++)
            sa[i] = q(sa[i].toUpperCase());
        return sa;
    }

    public static String q(String s) {
        if (s == null || s.length() == 0)
            return s;
        char c = s.charAt(0);
        if (c == '*' || c == '"')
            return s;
        return "\"" + s.toUpperCase() + "\"";
    }

    public static String vq(String s) {
        if (s == null || s.length() == 0)
            return s;
        char c = s.charAt(0);
        if (c == '*' || c == '\'')
            return s;
        return "\'" + s.toUpperCase() + "\'";
    }


    public static String excq(String s) {
        String re = "";
        String Q = "'";
        if (s == null)
            return "null";
        char[] ca = s.toCharArray();
        for (char c : ca) {
            re += (c == '\'') ? Q : "";
            re += c;
        }
        return Q + re + Q;
    }

    public static void printHash(Hashtable<String, Object> table) {
        for (String key : table.keySet())
            System.out.println(key + "=" + table.get(key));
    }

    public static String printSHash(Hashtable<String, Object> table) {
        StringBuffer sb = new StringBuffer();
        for (String key : table.keySet())
            sb.append(key + "=" + table.get(key) + "\n");
        return sb + "";
    }

    public static Thread runMethod(final Object o, final String method) {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    Method m = o.getClass().getMethod(method, new Class[] { });
                    m.invoke(o, new Object[] { });
                } catch (Exception e) {
                }
            }
        };
        Thread t = new Thread(r);
        t.setName(Thread.currentThread().getName());
        t.start();
        return t;
    }

    public static String fillZero(String s, int n) {
        String result = s;
        int n1 = n - s.length();
        for (int i = 0; i < n1; i++)
            result = '0' + result;
        return result;
    }

    public static String fillZeroR(String s, int n) {
        String result = s;
        int n1 = n - s.length();
        for (int i = 0; i < n1; i++)
            result = result + '0';
        return result;
    }

    public static String fillSpace(String s, int n) {
        String result = s;
        int n1 = n - s.length();
        for (int i = 0; i < n1; i++)
            result = result + ' ';
        return result;
    }

    public static String vq(String s, boolean isSq) {
        if (s.startsWith("##"))
            return s.substring(2);
        String qq = isSq ? "\'" : "\"";
        if (s == null || s.length() == 0)
            return s;
        char c = s.charAt(0);
        if (c == '*' || c == '\'' || c == '"')
            return s;
        c = s.charAt(1);
        if (c != '.')
            return qq + s + qq;
        qq = "\"";
        return s.substring(0, 2) + qq + s.substring(2) + qq;
    }

    public static String moneySign(String s) {
        float f = Float.parseFloat(s);
        DecimalFormat df = new DecimalFormat("$###,###.00");
        boolean eqz = f == 0.0f;
        return eqz ? "" : df.format(f);
    }

    public static String dayHours(long time) {
        String sec = two("" + time % 60, 2);
        time = time / 60;
        String min = two("" + time % 60, 2);
        time = time / 60;
        String hour = two("" + time % 24, 2);
        time = time / 24;
        String format = "%s:%s:%s";
        String message = printf(format, new String[] { hour, min, sec });
        message = (time == 0 ? "" : time + " Days ") + message;
        return message;
    }


    private static String two(String s, int n) {
        String result = s;
        int n1 = n - s.length();
        for (int i = 0; i < n1; i++)
            result = "0" + result;
        return result;
    }


    public static String getTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return time;
    }


    public static String showList(List list) {
        String re = "";
        for (Object object : list) {
            Map map = (Map) object;
            for (Object key : map.keySet())
                re += key + ":" + map.get(key) + "  ";
            re += "\n";
        }
        return re;
    }


    private static String replaceAll(String str, char c, char c1) {
        while (str.indexOf(c) != 0)
            str = str.replace(c, c1);
        return str;

    }

    public static final String CTXHASHTABLE = "CTXHASHTABLE";
    public static final String escQQ = "\\\"";
    public static final String OPTION_SFX = "SFX";
    public static final String OPTION_PFX = "PFX";
    public static final String OPTION_SSN = "SSN_TIN_CODE";
    public static final String OPTION_MARITAL_STAT = "MARITAL_STAT";
    public static final String OPTION_SP_EST_ANN_INC = "SP_EST_ANN_INC";
    public static final String OPTION_COUNTRY = "COUNTRY";
    public static final String OPTION_STATE = "STATE";
    public static final String OPTION_ID_DOC_TYPE = "ID_DOC_TYPE";
    public static final String OPTION_NOT_AVAILABLE = "NOT_AVAILABLE";
    public static final String OPTION_EE_RELT_TYPE = "BENE_RELT";
    public static final String OPTION_NUM3 = "NUM3";
    public static final String OPTION_NUM30 = "NUM30";

    public static String merge(Vector v, String fields) {
        Field field = null;
        String s0 = null;
        StringBuffer sb = new StringBuffer();
        String[] fa = Util.split(fields, "|");
        for (int j = 0; j < v.size(); j++) {
            Object o = v.get(j);
            Class oc = o.getClass();
            for (int i = 0; i < fa.length; i++) {
                s0 = "";
                try {
                    field = oc.getField(fa[i]);
                    s0 = (String) field.get(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append(strToHex(s0));
                if (i != fa.length - 1)
                    sb.append("|");
            }
            if (j != v.size() - 1)
                sb.append("^^^");
        }
        return sb.toString();
    }

    public static String hexToStr(String s) {
        StringBuffer sb = new StringBuffer();
        int n = 0;
        String s0;
        for (int i = 0; i < s.length() / 2; i++) {
            s0 = s.substring(i * 2, i * 2 + 2);
            n = Integer.parseInt(s0, 16);
            sb.append(new Character((char) n));
        }
        return sb.toString();
    }

    public static String strToHex(String s) {
        char c;
        if (s == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    public static String merge(String[] aa) {
        String result = "";
        for (int i = 0; i < aa.length; i++) {
            String[] bb = Util.split(aa[i], "|");
            result += bb[0] + "|";
        }
        return Util.removeLast(result, 1);
    }


    public static String printBeanMsg(Exception e, char type, Object bean, String name) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String et = "";
        switch (type) {
        case 'G':
            et = "Get Method";
            break;
        case 'g':
            et = "Get Method with arg of int";
            break;
        case 's':
            et = "Set Method";
            break;
        }
        return "Can not Find Method name=" + name + "<BR>bean_Class=" + bean.getClass() + "<BR>type=" + et + "<BR>" +
               sw.toString().substring(0, 400);
    }


    public static synchronized Hashtable getContext(String key) {
        Hashtable ht = getTable();
        Hashtable ctx = (Hashtable) ht.get(CTXHASHTABLE);
        return (Hashtable) ctx.get(key);
    }

    private static Hashtable utilTable = null;

    private static synchronized Hashtable getTable() {
        if (utilTable == null) {
            utilTable = new Hashtable();
            //makeSample();
            Hashtable ct = new Hashtable();
            utilTable.put(CTXHASHTABLE, ct);
            loadConfig();
        }
        return utilTable;
    }

    private static void loadConfig() {
        //putGbContext("table","cellSpacing=0 cellPadding=0 border=0");
        //putGbContext("row","class=signupcellcolor");
        //putGbContext("label.td","class=signuptext vAlign=bottom align=left");
        //putGbContext("div.td","class=signuptext  vAlign=bottom align=left");
        //putGbContext("array.td","class=signuptext vAlign=bottom align=left");
        //putGbContext("text","onblur=validate(this.form,{$jstable},{$id})");
    }


    public static void save(Hashtable h) {
        Hashtable ht = getTable();
        ht.putAll(h);
    }

    public static void save(String name, String userType, Vector v) {
        Hashtable ht = getTable();
        String sname = name + "." + userType;
        ht.put(sname.toLowerCase(), v);
    }

    /**
     * put global context here
     * with different element type
     */
    public static void putGbContext(String key, String value) {
        Hashtable t = Util.readHash(value);
        Hashtable ht = getTable();
        Hashtable ct = (Hashtable) ht.get(CTXHASHTABLE);
        ct.put(key.toLowerCase(), t);
    }

    /**
     * put all
     */
    public static void putGbContext(Hashtable ht) {
        Enumeration en = ht.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            putGbContext(key, (String) ht.get(key));
        }
    }

    public static String[] addArray(String[] sa, String val) {
        String[] re = new String[sa.length + 1];
        System.arraycopy(sa, 0, re, 0, sa.length);
        re[sa.length] = val;
        Arrays.sort(re);
        return re;
    }

    public static String printHex(int a) {
        short low = (short) (a & 0xFF);
        short high = (short) ((a / 0x100) & 0xFF);
        return printHex(high) + printHex(low);

    }

    public static String printHex(short a) {
        String result = String.format("%x", a).toUpperCase();
        if (result.length() == 1)
            result = "0" + result;
        if (result.length() == 4)
            result = result.substring(2, 4);
        return result;
    }

    public static String printInt(short a) {
        String result = String.format("%d", a).toUpperCase();
        if (result.length() == 1)
            result = "000" + result;
        if (result.length() == 2)
            result = "00" + result;
        if (result.length() == 3)
            result = "0" + result;
        return result;
    }

    public static String printBinary(byte a) {
        String re = "";
        int n = (int) a;
        for (int i = 0; i < 8; i++) {
            if ((n & 0x80) > 0)
                re += "1";
            else
                re += "0";
            n *= 2;
        }
        return re;
    }

    public static String printHex(byte[] a) {
        String re = "";
        for (int i = 0; i < a.length; i++)
            re += printHex(a[i]) + " ";
        return re;
    }

    public static String printHex(short[] a) {
        String re = "";
        for (int i = 0; i < a.length; i++)
            re += printHex(a[i]) + " ";
        return re;

    }

    public static String[] remove(String[] sa, String val) {
        Vector<String> v = new Vector();
        for (String key : sa) {
            if (key.equalsIgnoreCase(val))
                continue;
            v.add(key);
        }
        String[] re = getSArray(v);
        return re;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    
    

}
