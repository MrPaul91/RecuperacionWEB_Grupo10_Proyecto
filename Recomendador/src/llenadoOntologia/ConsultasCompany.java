package llenadoOntologia;
import virtuoso.jena.driver.*;

import org.apache.jena.query.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.*;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.DatasetImpl;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

import java.util.ArrayList;


public class ConsultasCompany {
	
public static void main(String []args) throws Exception{
		
		VirtGraph set = new VirtGraph("company","jdbc:virtuoso://50.18.123.50:1111","dba","dba");
		
		FileReader file = new FileReader("ConsultaCompany.txt");
		
		
		BufferedReader reader = new BufferedReader(file);
	    String s = reader.readLine();
	    ResultSet results = queryFactory.runQuery(s, "company");
	    
	   
	    Model model = ModelFactory.createDefaultModel();
	    InputStream fileOntology = FileManager.get().open("Ontology.owl");
	    
	    
	    model.read(fileOntology,null,"RDF/XML");
	    int indice = 0;

	    String URIOntology = "http://www.entrega1/ontologies/";	        
	    String URIDbpedia= "http://dbpedia.org/ontology/";	 
	        
	    ///Obtener la clase.
	    Resource classCompany = model.createResource(URIDbpedia+"Company");
	    ///Propiedades.
	    Property companyName_property = model.createProperty(URIOntology+"companyName");
	    Property address_property = model.createProperty(URIDbpedia+"address");
        Property sector_property = model.createProperty(URIOntology+"hasSector");
	        
	    while( results.hasNext()){
	    	QuerySolution soln = results.nextSolution();
	    	
	        String companyName = soln.getLiteral("nombreinstitucion").toString();
	        String address = soln.getLiteral("direccion").toString();
	        String sector = soln.getLiteral("sector").toString();
	        
	        ///Insercion en la ontologia.
	        
	        Resource individualCompany = model.createResource("compania"+""+indice,classCompany);
	        
	        individualCompany.addProperty(RDF.type, classCompany);

	        individualCompany.addProperty(companyName_property,companyName,XSDDatatype.XSDstring);
	          
	        individualCompany.addProperty(address_property,address,XSDDatatype.XSDstring);
  
	        individualCompany.addProperty(sector_property,sector,XSDDatatype.XSDstring);
	        
	        indice++;
	    }
	    
		   //Data TASK.
        
	       String SimulateArrayTask [] = new String[15];
	       
	       SimulateArrayTask[0] = "Stay effective inside a changing environment";
	       SimulateArrayTask[1] = "Stay effective facing new tasks, challenges and people";
	       SimulateArrayTask[2]	=  "Effectiveness to identify a problem and the pertinent data";
	       SimulateArrayTask[3]	=  "Recognice relevant information of a problem and possible causes";
	       SimulateArrayTask[4]	=  "Analyze and solve numeric issues";
	       SimulateArrayTask[5]		=  "Capacity to make actions to achive an advantage";
	       SimulateArrayTask[6]		=  "  Works by self satisfaction";
	       SimulateArrayTask[7]		=  " Need to achive an objective";
	       SimulateArrayTask[8]	  	=  " Detect client's expectations";
	       SimulateArrayTask[9]	 	=  "  Provide the required solutions to the client";
	       SimulateArrayTask[10]  	=  " Make decisions to assure control over methods, people and situations";
	       SimulateArrayTask[11] 	=  " Data evaluation and action lines to make logical decitions";
	       SimulateArrayTask[12] 	=  "Propose original and imaginative solutions";
	       SimulateArrayTask[13] =  "Innovation and identification of alternative methods";
	       SimulateArrayTask[14] = "Express clearly and convincingly with other persons";
		
	       
	    //Anadir Task al grafo.
	    
	       //Clase Task.
	       
	       
	       Resource task_class = model.createResource(URIDbpedia +"Task");
	       Property taskItem  = model.createProperty(URIOntology+"taskItem");
	       
	       Resource resourceTask [] = new Resource[15];
	       for (int i = 0; i < SimulateArrayTask.length; i++){
	    	   
	    	   Resource taskIndividual = model.createResource("task"+""+i,task_class);
	    	   taskIndividual.addProperty(RDF.type, task_class);
	    	   taskIndividual.addProperty(taskItem,SimulateArrayTask[i],XSDDatatype.XSDstring);
	    	   resourceTask[i] = taskIndividual;
	    	   
	    	   
	       }
		
	    //Data property hasSkill.
		//Consulta DBPEDIA.
		String service = "http://dbpedia.org/sparql";
	    ArrayList <Resource>skillResources = null;
	    String querySkill  = "PREFIX dbc: <http://dbpedia.org/resource/Category:> \r\n" +
	    	 	"SELECT DISTINCT ?skill WHERE {\r\n" + 
	    	 	" ?skill ?y dbc:Skills .\r\n" + 
	    	 	"}";
	    
	    
	           QueryExecution qe = QueryExecutionFactory.sparqlService(service, querySkill);
	            try{
	                ResultSet resultsSkill = qe.execSelect();
	                
	                int indiceC = 0;
	                
	                
	               Resource skill_class = model.createResource(URIDbpedia +"Skill");
	     	       Property hasTask  = model.createProperty(URIOntology+"hasTask");
	     	       Property nameOfSkill  = model.createProperty(URIOntology+"nameOfSkill");
	     	       
	     	       skillResources = new ArrayList();
	     	       
	                while(resultsSkill.hasNext()){
	                    QuerySolution sol = (QuerySolution) resultsSkill.next();
	                    //Aca se obtiene el Skill.
	                    String skillValue = sol.get("?skill").toString();
	                    int Random = (int) (Math.random() * 14);
	                    Resource skillIndividual = model.createResource("skill"+""+indiceC,skill_class);
	                    skillIndividual.addProperty(RDF.type, skill_class);
	                    skillIndividual.addProperty(hasTask,resourceTask[Random]);
	                    skillIndividual.addProperty(taskItem,skillValue,XSDDatatype.XSDstring);
	                    
	                    skillResources.add(skillIndividual);
	                    indiceC++; 
	                    
	                }
	                
	            }
	            catch(Exception e){
	                System.out.println(e.getMessage());
	            }
	            finally{
	                qe.close();
	            }
		
		
		
	

	       ///Llenado de PERSONA.
	        file = new FileReader("ConsultaPersonas.txt");
			reader = new BufferedReader(file);

			
			ResultSet resultsPerson = null;
			
			try{
				results = executeQuery(reader.readLine());
			}
			catch(Exception e){
				
				System.out.println(e.getMessage());
			
			}
			if (results != null){
				 
				 Resource person_withJob = model.createResource(URIOntology +"PersonWithJob");
				 Resource person_withOutJob = model.createResource(URIOntology +"PersonWithoutJob");
	     	     Property hasSkill  = model.createProperty(URIOntology+"hasSkill");
	     	     Property birthName  = model.createProperty(URIDbpedia+"birthName");
	     	       
	     	     Resource job_Position = model.createResource(URIOntology +"jobPosition");  
	     	     Property jobName = model.createProperty(URIOntology+"jobName");
	     	     
	     	     int indiceX = 0;
	     	     
				 while(results.hasNext()) {
				      QuerySolution soln = results.nextSolution();
				      
				
				       String Name = soln.getLiteral("birthName").toString();
				       String tieneEmpleo = soln.getLiteral("hasEmployment").toString();
				       String stringJob = soln.getLiteral("jobName").toString();
				       
				       if(tieneEmpleo.equals("1")){
				    	   Resource personWithJobIndividual = model.createResource("person"+indiceX,person_withJob);
		                   
				    	   
				    	   personWithJobIndividual.addProperty(birthName,Name,XSDDatatype.XSDstring);
				    	   
				    	   //Generar skill Aleatorio.
				    	   
				 
				    	   int aleatorio = ((int) (Math.random() * (skillResources.size()-1)));
				    	   
				    	   personWithJobIndividual.addProperty(birthName,Name,XSDDatatype.XSDstring);
				    	   personWithJobIndividual.addProperty(hasSkill, skillResources.get(aleatorio));
				    	   
				    	   personWithJobIndividual.addProperty(RDF.type,person_withJob);
				    	   Resource jobIndividual = model.createResource("jobPosition"+indiceX,job_Position);
				    	   jobIndividual.addProperty(jobName,stringJob, XSDDatatype.XSDstring);
		                   jobIndividual.addProperty(RDF.type,job_Position);
				    	   
				       }
				       else{
				    	   
                            Resource personWithJobIndividual = model.createResource("person"+indiceX,person_withOutJob);
		                   
				    	   
				    	   personWithJobIndividual.addProperty(birthName,Name,XSDDatatype.XSDstring);
				    	   
				    	   //Generar skill Aleatorio.
				    	   
				 
				    	   int aleatorio = ((int) (Math.random() * (skillResources.size()-1)));
				    	   
				    	   personWithJobIndividual.addProperty(birthName,Name,XSDDatatype.XSDstring);
				    	   personWithJobIndividual.addProperty(hasSkill, skillResources.get(aleatorio));
				    	   
				    	   personWithJobIndividual.addProperty(RDF.type,person_withOutJob);
				    	   Resource jobIndividual = model.createResource("jobPosition"+indiceX,job_Position);
				    	   jobIndividual.addProperty(jobName,stringJob, XSDDatatype.XSDstring);
		                   jobIndividual.addProperty(RDF.type,job_Position);
				    	   
				    	   
				       }
				      
				       
				       indiceX++;
				       
				 }
		    } 

	       
	   OutputStream ActualizacionOntologia = new FileOutputStream("Ontologyout.owl");
	
	   model.write(ActualizacionOntologia,"TURTLE");
	     
	   //zSystem.out.println("Entro");	
	}



	public static ResultSet executeQuery(String queryString) throws Exception {
	
		 QueryExecution exec =  QueryExecutionFactory.create(QueryFactory.create(queryString), new
				 DatasetImpl(ModelFactory.createDefaultModel()));
		 return exec.execSelect();
		 
	}

}
