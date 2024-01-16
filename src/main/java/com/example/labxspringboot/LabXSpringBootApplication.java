package com.example.labxspringboot;

import com.example.labxspringboot.entity.*;
import com.example.labxspringboot.entity.enume.RoleUser;
import com.example.labxspringboot.entity.enume.StatusAnalyse;
import com.example.labxspringboot.entity.enume.StatusResultat;
import com.example.labxspringboot.entity.enume.TypeAnalyse;
import com.example.labxspringboot.service.impl.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class LabXSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabXSpringBootApplication.class, args);
    }


    @Bean
    CommandLineRunner start(NormeServiceImpl normeService, UtilisateurServiceImpl utilisateurService, EchantillonServiceImpl echantillonService, PatientServiceImpl patientService, MaterialEchanServiceImpl materialEchanService, ReactifServiceImpl reactifService, AnalyseServiceImpl analyseService, TestAnalyseServiceImpl testAnalyseService) {
        return args -> {

            Patient patient = new Patient();
            patient.setNom("TestPatient");
            patient.setPrenom("khona");
            patient.setDateNaissance("2002");
            patient.setSexe("homme");
            patient.setAdresse("casa");
            patient.setNumeroTelephone("044654645");
            patientService.savePatient(patient);

            // Create a Technicien
            Technicien technicien = new Technicien();
            technicien.setNomUtilisateur("miko");
            technicien.setMotDePasse("152");
            technicien.setSpecialiteTechnicien("technicien");
            technicien.setRole(RoleUser.TECHNICIEN);
            utilisateurService.saveUtilisateur(technicien);

            ResponsableLabo responsableLabo = new ResponsableLabo();
            responsableLabo.setNomUtilisateur("TestResponsable");
            responsableLabo.setMotDePasse("1234");
            responsableLabo.setFonctionResponsable("responsable");
            responsableLabo.setRole(RoleUser.RESPONSABLE_LABORATOIRE);
            utilisateurService.saveUtilisateur(responsableLabo);

            // Create a MaterielEchan
            MaterielEchan materielEchan = new MaterielEchan("TestMaterielEchan", 10, "2025-01-01", "TestFournisseur");
            materialEchanService.saveMaterialEchan(materielEchan);

            Reactif testReactif = new Reactif();
            testReactif.setNom("twacha");
            testReactif.setQuantiteStock(12);
            testReactif.setDateExpiration("2025");
            testReactif.setFournisseurNom("mima");
            reactifService.saveReactif(testReactif);

            // Create an Echantillon
            Echantillon testEchantillon = new Echantillon(patient, materielEchan, new ArrayList<>(), technicien, "2023-01-01");
            testEchantillon.setTechnicienEch(technicien);
            testEchantillon.setMaterielEchan(materielEchan);
            testEchantillon.setPatient(patient);
            echantillonService.saveEchantillon(testEchantillon);

            // Create and save an Analyse
            Analyse testAnalyse = new Analyse(testEchantillon, null, null, TypeAnalyse.HEMATOLOGIE, "2022", "2023", StatusResultat.NORMAL, StatusAnalyse.EN_COURS_ANALYSE, "Test Comment");
            testAnalyse.setResponsableLaboAnalyse(responsableLabo);
            analyseService.saveAnalyse(testAnalyse);

            // Create test
            TestAnalyse testAnalyse1 = new TestAnalyse();
            testAnalyse1.setResponsableLaboTest(responsableLabo);
            testAnalyse1.setAnalyse(testAnalyse);
            testAnalyse1.setReactif(testReactif);
            testAnalyse1.setDescription("lhyt");
            Norme norme = normeService.getNormeById(10L);
            testAnalyse1.setNorme(norme);
            testAnalyse1.setResultatNmbr(80);
            testAnalyseService.saveTestAnalyse(testAnalyse1);

            // Check if the result is within the normal range
            if (testAnalyse1.getResultatNmbr() >= norme.getMin() && testAnalyse1.getResultatNmbr() <= norme.getMax()) {
                // Result is normal
                testAnalyse1.setStatusResultat(StatusResultat.NORMAL);
            } else {
                // Result is abnormal
                testAnalyse1.setStatusResultat(StatusResultat.ANORMAL);
            }

            // Save or update the TestAnalyse object
            testAnalyseService.saveTestAnalyse(testAnalyse1);

            System.out.println(testAnalyse1);
        };
    }

}
