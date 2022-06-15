package me.Zcamt.zgangs.objects.gang.motd;

import me.Zcamt.zgangs.objects.gang.Gang;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GangMotd {

    private Gang gang;
    private @Nullable String line1;
    private @Nullable String line2;
    private @Nullable String line3;
    private @Nullable String line4;
    private @Nullable String line5;
    private @Nullable String line6;
    private @Nullable String line7;

    public GangMotd(@Nullable String line1,
                    @Nullable String line2,
                    @Nullable String line3,
                    @Nullable String line4,
                    @Nullable String line5,
                    @Nullable String line6,
                    @Nullable String line7) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.line5 = line5;
        this.line6 = line6;
        this.line7 = line7;
    }


    public void setLine1(@Nullable String line1) {
        this.line1 = line1;
        gang.serialize();
    }

    public void setLine2(@Nullable String line2) {
        this.line2 = line2;
        gang.serialize();
    }

    public void setLine3(@Nullable String line3) {
        this.line3 = line3;
        gang.serialize();
    }

    public void setLine4(@Nullable String line4) {
        this.line4 = line4;
        gang.serialize();
    }

    public void setLine5(@Nullable String line5) {
        this.line5 = line5;
        gang.serialize();
    }

    public void setLine6(@Nullable String line6) {
        this.line6 = line6;
        gang.serialize();
    }

    public void setLine7(@Nullable String line7) {
        this.line7 = line7;
        gang.serialize();
    }


    @Nullable
    public String getLine1() {
        return line1;
    }

    @Nullable
    public String getLine2() {
        return line2;
    }

    @Nullable
    public String getLine3() {
        return line3;
    }

    @Nullable
    public String getLine4() {
        return line4;
    }

    @Nullable
    public String getLine5() {
        return line5;
    }

    @Nullable
    public String getLine6() {
        return line6;
    }

    @Nullable
    public String getLine7() {
        return line7;
    }

    public List<String> getFullMotd() {
        ArrayList<String> fullMotd = new ArrayList<>();
        if(line1 != null) {
            fullMotd.add(line1);
        }
        if(line2 != null) {
            fullMotd.add(line2);
        }
        if(line3 != null) {
            fullMotd.add(line3);
        }
        if(line4 != null) {
            fullMotd.add(line4);
        }
        if(line5 != null) {
            fullMotd.add(line5);
        }
        if(line6 != null) {
            fullMotd.add(line6);
        }
        if(line7 != null) {
            fullMotd.add(line7);
        }
        return fullMotd;
    }

    public void setGang(Gang gang) {
        if(this.gang == null && gang != null) {
            this.gang = gang;
        }
    }

}
