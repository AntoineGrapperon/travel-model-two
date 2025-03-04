package com.pb.mtctm2.abm.ctramp;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.pb.common.calculator.IndexValues;
import com.pb.common.calculator.VariableTable;
import com.pb.mtctm2.abm.accessibilities.AccessibilitiesTable;
import com.pb.mtctm2.abm.accessibilities.BuildAccessibilities;

public abstract class DestChoiceTwoStageModelDMU
        implements Serializable, VariableTable
{

    protected transient Logger         logger   = Logger.getLogger(DestChoiceTwoStageModelDMU.class);

    protected HashMap<String, Integer> methodIndexMap;

    protected Household                hh;
    protected Person                   person;
    protected Tour                     tour;
    protected IndexValues              dmuIndex = null;

    protected double                   workAccessibility;
    protected double                   nonMandatoryAccessibility;

    protected double[]                 homeMgraNonMandatoryAccessibilityArray;
    protected double[]                 homeMgraTotalEmploymentAccessibilityArray;

    protected int[]                    sampleMgras;
    protected double[]                 modeChoiceLogsums;
    protected double[]                 dcSoaCorrections;

    protected double[]                 mgraSizeArray;
    protected double[]                 mgraDistanceArray;
    
    protected int                      toursLeftCount;
    
    protected ModelStructure           modelStructure;
    protected MgraDataManager          mgraManager;
    protected BuildAccessibilities     aggAcc;
    protected AccessibilitiesTable     accTable;

    public DestChoiceTwoStageModelDMU(ModelStructure modelStructure)
    {
        this.modelStructure = modelStructure;
        initDmuObject();
    }

    abstract public void setMcLogsum(int mgra, double logsum);

    private void initDmuObject()
    {

        dmuIndex = new IndexValues();

        // create default objects - some choice models use these as place holders for values
        person = new Person(null, -1, modelStructure);
        hh = new Household(modelStructure);

        mgraManager = MgraDataManager.getInstance();

        int maxMgra = mgraManager.getMaxMgra();

        modeChoiceLogsums = new double[maxMgra + 1];
        dcSoaCorrections = new double[maxMgra + 1];

    }

    public void setHouseholdObject(Household hhObject)
    {
        hh = hhObject;
    }

    public void setPersonObject(Person personObject)
    {
        person = personObject;
    }

    public void setTourObject(Tour tour)
    {
        this.tour = tour;
    }

    public void setAggAcc(BuildAccessibilities aggAcc)
    {
        this.aggAcc = aggAcc;
    }

    public void setAccTable(AccessibilitiesTable myAccTable)
    {
        accTable = myAccTable;
    }

    public void setMgraSizeArray( double[] mgraSizeArray )
    {
        this.mgraSizeArray = mgraSizeArray;
    }

    public void setMgraDistanceArray(double[] mgraDistanceArray)
    {
        this.mgraDistanceArray = mgraDistanceArray;
    }
    
    public void setSampleArray( int[] sampleArray )
    {
        sampleMgras = sampleArray;
    }

    public void setDcSoaCorrections( double[] sampleCorrections )
    {
        dcSoaCorrections = sampleCorrections;
    }

    public void setNonMandatoryAccessibility(double nonMandatoryAccessibility)
    {
        this.nonMandatoryAccessibility = nonMandatoryAccessibility;
    }

    public void setToursLeftCount(int count)
    {
        toursLeftCount = count;
    }
    
    public void setDmuIndexValues(int hhId, int zoneId, int origTaz, int destTaz)
    {
        dmuIndex.setHHIndex(hhId);
        dmuIndex.setZoneIndex(zoneId);
        dmuIndex.setOriginZone(origTaz);
        dmuIndex.setDestZone(destTaz);

        dmuIndex.setDebug(false);
        dmuIndex.setDebugLabel("");
        if (hh.getDebugChoiceModels())
        {
            dmuIndex.setDebug(true);
            dmuIndex.setDebugLabel("Debug DC UEC");
        }

    }

    public IndexValues getDmuIndexValues()
    {
        return dmuIndex;
    }

    public Household getHouseholdObject()
    {
        return hh;
    }

    public Person getPersonObject()
    {
        return person;
    }

    // DMU methods - define one of these for every @var in the mode choice control
    // file.

    protected int getToursLeftCount()
    {
        return toursLeftCount;
    }
    
    protected int getMaxContinuousAvailableWindow() {

        if ( tour.getTourCategory().equalsIgnoreCase(ModelStructure.JOINT_NON_MANDATORY_CATEGORY))
            return hh.getMaxJointTimeWindow( tour );
        else
            return person.getMaximumContinuousAvailableWindow();
    }
    
    protected double getDcSoaCorrectionsAlt(int alt)
    {
        return dcSoaCorrections[alt-1];
    }

    protected double getMcLogsumDestAlt(int alt)
    {
        return modeChoiceLogsums[alt-1];
    }

    protected double getPopulationDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraPopulation(mgra);
    }

    protected double getHouseholdsDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraHouseholds(mgra);
    }

    protected double getGradeSchoolEnrollmentDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraGradeSchoolEnrollment(mgra);
    }

    protected double getHighSchoolEnrollmentDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraHighSchoolEnrollment(mgra);
    }

    protected double getUniversityEnrollmentDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraUniversityEnrollment(mgra);
    }

    protected double getOtherCollegeEnrollmentDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraOtherCollegeEnrollment(mgra);
    }

    protected double getAdultSchoolEnrollmentDestAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return aggAcc.getMgraAdultSchoolEnrollment(mgra);
    }

    protected int getIncome()
    {
        return hh.getIncome();
    }

    protected int getIncomeInDollars()
    {
        return hh.getIncomeInDollars();
    }

    protected int getAutos()
    {
        return hh.getAutosOwned();
    }

    protected int getWorkers()
    {
        return hh.getWorkers();
    }

    protected int getNumberOfNonWorkingAdults()
    {
        return hh.getNumberOfNonWorkingAdults();
    }

    protected int getNumPreschool()
    {
        return hh.getNumPreschool();
    }

    public int getNumGradeSchoolStudents()
    {
        return hh.getNumGradeSchoolStudents();
    }
    
    public int getNumHighSchoolStudents()
    {
        return hh.getNumHighSchoolStudents();
    }
    
    
    protected int getNumChildrenUnder16()
    {
        return hh.getNumChildrenUnder16();
    }

    protected int getNumChildrenUnder19()
    {
        return hh.getNumChildrenUnder19();
    }

    protected int getAge()
    {
        return person.getAge();
    }

    protected int getFemaleWorker()
    {
        if (person.getPersonIsFemale() == 1) return 1;
        else return 0;
    }

    protected int getFemale()
    {
        if (person.getPersonIsFemale() == 1) return 1;
        else return 0;
    }

    protected int getFullTimeWorker()
    {
        if (person.getPersonIsFullTimeWorker() == 1) return 1;
        else return 0;
    }

    protected int getTypicalUniversityStudent()
    {
        return person.getPersonIsTypicalUniversityStudent();
    }

    protected int getPersonType()
    {
        return person.getPersonTypeNumber();
    }

    protected int getPersonHasBachelors()
    {
        return person.getHasBachelors();
    }

    protected int getPersonIsWorker()
    {
        return person.getPersonIsWorker();
    }

    protected int getWorkTaz()
    {
        return person.getUsualWorkLocation();
    }

    protected int getWorkTourModeIsSOV()
    {
        boolean tourModeIsSov = modelStructure.getTourModeIsSov(tour.getTourModeChoice());
        if (tourModeIsSov) return 1;
        else return 0;
    }

    protected int getTourIsJoint()
    {
        return tour.getTourCategory().equalsIgnoreCase(ModelStructure.JOINT_NON_MANDATORY_CATEGORY) ? 1 : 0;
    }

    protected double getTotEmpAccessibilityAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return homeMgraTotalEmploymentAccessibilityArray[mgra];
    }

    protected double getNonMandatoryAccessibilityAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return accTable.getAggregateAccessibility("nonmotor", mgra);
    }

    protected double getOpSovDistanceAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return mgraDistanceArray[mgra];
    }

    protected double getLnDcSizeAlt(int alt)
    {
        int mgra = sampleMgras[alt-1];
        return Math.log(mgraSizeArray[mgra] + 1);
    }

    protected void setWorkAccessibility(double accessibility)
    {
        workAccessibility = accessibility;
    }

    protected double getWorkAccessibility()
    {
        return workAccessibility;
    }

    protected double getNonMandatoryAccessibility()
    {
        return nonMandatoryAccessibility;
    }

    public int getIndexValue(String variableName)
    {
        return methodIndexMap.get(variableName);
    }

    public int getAssignmentIndexValue(String variableName)
    {
        throw new UnsupportedOperationException();
    }

    public double getValueForIndex(int variableIndex)
    {
        throw new UnsupportedOperationException();
    }

    public double getValueForIndex(int variableIndex, int arrayIndex)
    {
        throw new UnsupportedOperationException();
    }

    public void setValue(String variableName, double variableValue)
    {
        throw new UnsupportedOperationException();
    }

    public void setValue(int variableIndex, double variableValue)
    {
        throw new UnsupportedOperationException();
    }

}
