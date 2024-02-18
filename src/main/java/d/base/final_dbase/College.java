package d.base.final_dbase;

public record College(String code, String name) {
    @Override
    public String toString() {
        return "(" + code + ") " + name;
    }
}