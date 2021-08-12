package cn.idu.plugin.transform;

public class MethodTimeEntity {

    public MethodTimeEntity(int access, String name, String desc, String tag) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.tag = tag;
    }

    public int access;

    public String name;

    public String desc;

    public String tag;

    @Override
    public String toString() {
        return "MethodTimeEntity{" +
                "access=" + access +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    boolean isSame(int access, String name, String desc) {
        return this.access == access && this.name.equals(name) && this.desc.equals(desc);
    }
}
