package com.modular.fuzzy;

import java.lang.Math;

public class BiblioFuzzy {
    public static double trapecioAbiertoDer(double u, double a, double b){
        if(u > b){
            return 1.0;
        }
        if(u < a){
            return 0;
        }
        if(a <= u && u <= b){
            return (u-a) / (b-a);
        }
        return -1;
    }

    public static double trapecioAbiertoIzq(double u, double a, double b){
        if(u > b){
            return 0.0;
        }
        if(u < a){
            return 1.0;
        }
        if(a <= u && u <= b){
            return (b-u) / (b-a);
        }
        return -1;
    }

    public static double triangular(double u, double a, double b, double c){
        if(u < a || u > c){
            return 0.0;
        }
        if(a <= u && u < b){
            return (u - a) / (b - a);
        }
        if(b <= u && u <= c){
            return (c - u) / (c - b);
        }
        return -1;
    }

    public static double trapezoidal(double u, double a, double b, double c, double d){
        if(u < a || u > d){
            return 0.0;
        }
        if(b <= u && u <= c){
            return 1.0;
        }
        if(a <= u && u < b){
            return (u - a) / (b - a);
        }
        if(c < u && u <= d){
            return (d - u) / (d - c);
        }
        return -1;
    }

    public static double curvaS(double u, double a, double b){
        if(u > b){
            return 1.0;
        }
        if(u < a){
            return 0.0;
        }
        if(a <= u && u <= b){
            return (1 + Math.cos(((u - b) / (b - a)) * Math.PI)) / 2.0;
        }
        return -1;
    }

    public static double curvaZ(double u, double a, double b){
        if(u > b){
            return 0.0;
        }
        if(u < a){
            return 1.0;
        }
        if(a <= u && u <=b){
            return (1 + Math.cos(((u - a) / (b - a)) * Math.PI)) / 2.0;
        }
        return -1;
    }

    public static double triangularSuave(double u, double a, double b, double c){
        if(u < a || u > c){
            return 0.0;
        }
        if(a <= u && u < b){
            return (1 + Math.cos(((u - b) / (b - a)) * Math.PI)) / 2.0;
        }
        if(b <= u && u <= c){
            return (1 + Math.cos(((b - u) / (c - b)) * Math.PI)) / 2.0;
        }
        return -1;
    }

    public static double trapezoidalSuave(double u, double a, double b, double c, double d){
        if(u < a || u > d){
            return 0.0;
        }
        if(b <= u && u <= c){
            return 1.0;
        }
        if(a <= u && u < b){
            return (1 + Math.cos(((u - b) / (b - a)) * Math.PI)) / 2.0;
        }
        if(c < u && u <= d){
            return (1 + Math.cos(((c - u) / (d - c)) * Math.PI)) / 2.0;
        }
        return -1;
    }

    //Accesorios de apoyo

    public static double min(double a, double b){
        if(a < b){
            return a;
        }
        return b;
    }

    public static double max(double a, double b){
        if(a > b){
            return a;
        }
        return b;
    }

    //Operadores logicos Fuzzy
    public static double compAnd(double maU, double mbU){
        return min(maU, mbU);
    }

    public static double compOr(double maU, double mbU){
        return max(maU, mbU);
    }

    public static double niega(double maU){
        return 1.0 - maU;
    }

    //Implicacion fuzzy
    public static double implicaZadeh(double maX, double mbY){
        return max(min(maX, mbY), niega(maX));
    }

    public static double implicaMamdani(double maX, double mbY){
        return min(maX, mbY);
    }

    public static double implicaGodel(double maX, double mbY){
        if(maX <= mbY){
            return 1;
        }
        return mbY;
    }

    public static void main(String[] args) {
        final int li=0,ld=30,lsi=10,lsd=20,lc=15;
        int u=0;
        System.out.println("********** Trapecio Abierto por la Izquierda **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", TAI(u): "+BiblioFuzzy.trapecioAbiertoIzq(u, li+10, ld-10));
        }
        System.out.println("\n********** Trapecio Abierto por la Derecha **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", TAD(u): "+BiblioFuzzy.trapecioAbiertoDer(u, li+10, ld-10));
        }

        System.out.println("\n********** Triangular **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", Triang(u): "+BiblioFuzzy.triangular(u, li+10, lc, ld-10));
        }

        System.out.println("\n********** Trapezoidal **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", Trap(u): "+BiblioFuzzy.trapezoidal(u, li+5, lsi, lsd, ld-5));
        }

        System.out.println("\n********** Curva Z **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", CZ(u): "+BiblioFuzzy.curvaZ(u, li+5, ld-5));
        }

        System.out.println("\n********** Curva S **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", CS(u): "+BiblioFuzzy.curvaS(u, li+5, ld-5));
        }

        System.out.println("\n********** Triangular Suave**********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", TS(u): "+BiblioFuzzy.triangularSuave(u, li+5, lc, ld-5));
        }

        System.out.println("\n********** Trapezoidal Suave **********");
        for(u=li;u<ld;u++){
            System.out.println("Para u="+u+", TrapS(u): "+BiblioFuzzy.trapezoidalSuave(u, li+5, lsi, lsd, ld-5));
        }
    }
}
