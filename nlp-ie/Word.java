/**
 * 
 */

/**
 * @author Prafulla
 *
 */
public class Word {

	String POS;
	String wordName;
	String ingForm;
	String spForm;
	String ppForm;
	String category;
	String pluralForm;
	
	
	public String getPOS() {
		return POS;
	}
	public void setPOS(String pOS) {
		POS = pOS;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public String getIngForm() {
		return ingForm;
	}
	public void setIngForm(String ingForm) {
		this.ingForm = ingForm;
	}
	public String getSpForm() {
		return spForm;
	}
	public void setSpForm(String spForm) {
		this.spForm = spForm;
	}
	public String getPpForm() {
		return ppForm;
	}
	public void setPpForm(String ppForm) {
		this.ppForm = ppForm;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPluralForm() {
		return pluralForm;
	}
	public void setPluralForm(String pluralForm) {
		this.pluralForm = pluralForm;
	}
	
	public boolean contains(String token)
	{
		if(token.equalsIgnoreCase(this.getWordName())|| token.equalsIgnoreCase(this.getSpForm())
				||token.equalsIgnoreCase(this.getPpForm()) ||token.equalsIgnoreCase(this.getIngForm())
				||token.equalsIgnoreCase(this.getPluralForm()))
						{
							return true;
						}

		return false;
	}
}
