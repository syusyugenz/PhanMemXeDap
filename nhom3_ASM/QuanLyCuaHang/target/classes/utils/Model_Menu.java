package utils;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Model_Menu {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public Model_Menu() {
    }

    public Model_Menu(String name, String icon, MenuType type) {
        this.name = name;
        this.icon = icon;
        this.type = type;
    }   

    String name;
    String icon;
    MenuType type;
    
    public Icon toIcon(){
        return XImage.read(icon + ".png");
    }

    public static enum MenuType {
        MENU, EMPTY;
    }
}
