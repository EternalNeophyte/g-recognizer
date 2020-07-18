import java.util.ArrayList;
import java.util.List;

public class FragmentSpreader {

    private int nCores;
    private int coresAtX;
    private int coresAtY;
    private List<Integer> valuesAtX;
    private List<Integer> valuesAtY;
    private static final int DEFAULT_VALUE = 1;

    public FragmentSpreader(int nCores) {
        this.nCores = nCores;
        coresAtX = DEFAULT_VALUE;
        coresAtY = DEFAULT_VALUE;
        valuesAtX = new ArrayList<>();
        valuesAtY = new ArrayList<>();
        spreadCores();
    }

    public int getnCores() {
        return nCores;
    }

    public void setnCores(int nCores) {
        this.nCores = nCores;
    }

    public int getCoresAtX() {
        return coresAtX;
    }

    public void setCoresAtX(int coresAtX) {
        this.coresAtX = coresAtX;
    }

    public int getCoresAtY() {
        return coresAtY;
    }

    public void setCoresAtY(int coresAtY) {
        this.coresAtY = coresAtY;
    }

    public List<Integer> getValuesAtX() {
        return valuesAtX;
    }

    public void setValuesAtX(List<Integer> valuesAtX) {
        this.valuesAtX = valuesAtX;
    }

    public List<Integer> getValuesAtY() {
        return valuesAtY;
    }

    public void setValuesAtY(List<Integer> valuesAtY) {
        this.valuesAtY = valuesAtY;
    }

    private void spreadCores() {
        int difference = nCores;
        for(int i = 1; i <= nCores / 2; i++) {
            if(nCores % i == 0 && difference >= Math.abs(i - (nCores / i))) {
                coresAtX = i;
                coresAtY = nCores / coresAtX;
                difference = Math.abs(coresAtX - coresAtY);
            }
        }
    }

    public void spreadAllValues(int maxValueAtX, int maxValueAtY) {
        spreadValuesAtDimension(valuesAtX, maxValueAtX, coresAtX);
        spreadValuesAtDimension(valuesAtY, maxValueAtY, coresAtY);
    }

    private void spreadValuesAtDimension(List<Integer> valuesAtDimension, int maxValueAtDimension, int coresAtDimension) {
        valuesAtDimension.add(0);
        for(int i = maxValueAtDimension / coresAtDimension; i <= maxValueAtDimension; i += (maxValueAtDimension / coresAtDimension)) {
            valuesAtDimension.add(i);
        }
        if(valuesAtDimension.get(valuesAtDimension.size() - 1) != maxValueAtDimension) {
            valuesAtDimension.set(valuesAtDimension.size() - 1, maxValueAtDimension);
        }
    }
}
