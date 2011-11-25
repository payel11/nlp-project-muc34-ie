import java.util.ArrayList;





public class Template {

	String id;
	String incident;
	String weapon;
	String perpetratorPerson;
	String PerpetratorOrg;
	String target;
	String victim;
	
	public Template() 
	{
		this.id = "-";
		this.incident = "-";
		this.weapon = "-";
		this.perpetratorPerson = "-";
		this.PerpetratorOrg="-";
		this.target="-";
		this.victim= "-";
	}

	public void setVictim(String g)
	{
		this.victim = g;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIncident() {
		return incident;
	}
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	public void setIncident(String incident) {
		this.incident = incident;
	}
	public String getPerpetratorPerson() {
		return perpetratorPerson;
	}
	public void setPerpetratorPerson(String perpetratorPerson) {
		this.perpetratorPerson = perpetratorPerson;
	}
	public String getPerpetratorOrg() {
		return PerpetratorOrg;
	}
	public void setPerpetratorOrg(String perpetratorOrg) {
		PerpetratorOrg = perpetratorOrg;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getVictim() {
		return victim;
	}
	public void addVictim(String v)
	{
		if(this.getVictim().equalsIgnoreCase("-"))
		{
			this.setVictim(v);
		}
		else
		{
			this.setVictim(this.getVictim()+","+v);
	
		}
	}

	public static Template getTemplate()
	{
		return new Template();
	}
	public void printTemplate()
	{
		System.out.print("\n\nID: "+this.getId()+"\nINCIDENT: "+this.getIncident());
		System.out.print("\nWEAPON: "+this.getWeapon()+"\nPERP INDIV: "+this.getPerpetratorPerson());
		System.out.print("\nPERP ORG: "+this.getPerpetratorOrg()+"\nTARGET: "+this.getTarget());
		System.out.print("\nVICTIM	: ");
		printVictims();
		
	}
	
	private void printVictims()
	{
		String v []= this.getVictim().split(",");
		int vSize = v.length;
		for(int i=0;i<vSize;i++)
		{
			System.out.print(v[i]+"\n");
		}
	}
}
