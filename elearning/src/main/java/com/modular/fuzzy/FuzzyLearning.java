package com.modular.fuzzy;

public class FuzzyLearning {
    private static String [] aprendizajeSet = {"Poco", "Medio", "Alto"};

    private static double [] visual = new double[3];
    private static double [] auditivo = new double[3];
    private static double [] kinestesico = new double[3];

    private static int posNivMemMay(double [] nivsMem){
        int posMay=0;
        for(int i=0; i<nivsMem.length; i++){
            if (nivsMem[i]>nivsMem[posMay]){
                posMay=i;
            }
        }
        return posMay;
    }

    public static void memberShipVisual(double visualPoints){
        visual[0]=BiblioFuzzy.curvaZ(visualPoints, 7, 14);
        visual[1]=BiblioFuzzy.triangularSuave(visualPoints, 7, 14, 21);
        visual[2]=BiblioFuzzy.curvaS(visualPoints, 14, 21);
    }


    public static void memberShipAuditivo(double auditivoPoints){
        auditivo[0]=BiblioFuzzy.curvaZ(auditivoPoints, 7, 14);
        auditivo[1]=BiblioFuzzy.triangularSuave(auditivoPoints, 7, 14, 21);
        auditivo[2]=BiblioFuzzy.curvaS(auditivoPoints, 14, 21);
    }


    public static void memberShipKinestesico(double kinestesicoPoints){
        kinestesico[0]=BiblioFuzzy.curvaZ(kinestesicoPoints, 7, 14);
        kinestesico[1]=BiblioFuzzy.triangularSuave(kinestesicoPoints, 7, 14, 21);
        kinestesico[2]=BiblioFuzzy.curvaS(kinestesicoPoints, 14, 21);
    }

    public static String fuzzyVisual(double visualPoints){
        String set = "";
        memberShipVisual(visualPoints);
        set = aprendizajeSet[posNivMemMay(visual)];
        return set;
    }

    public static String fuzzyAuditivo(double auditivoPoints){
        String set = "";
        memberShipAuditivo(auditivoPoints);
        set = aprendizajeSet[posNivMemMay(auditivo)];
        return set;
    }

    public static String fuzzyKinestesico(double kinestesicoPoints){
        String set = "";
        memberShipKinestesico(kinestesicoPoints);
        set = aprendizajeSet[posNivMemMay(kinestesico)];
        return set;
    }

    public static String inferirAprendizaje(String diffuseVisual, String diffuseAuditivo, String diffuseKinestesico){
        String aprendizaje = "Visual";
        if(diffuseVisual.equals("Alto") || diffuseVisual.equals("Medio")){
            aprendizaje = "Visual";
        }
        if(diffuseAuditivo.equals("Alto") || diffuseAuditivo.equals("Medio")){
            aprendizaje = "Auditivo";
        }
        if(diffuseKinestesico.equals("Alto") || diffuseKinestesico.equals("Medio")){
            aprendizaje = "Kinestesico";
        }
        if(diffuseVisual.equals("Poco") && diffuseAuditivo.equals("Poco") && diffuseKinestesico.equals("Poco")){
            aprendizaje = "Visual";
        }
        return aprendizaje;
    }
}
