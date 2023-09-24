package winkhouse.xmldeser.utils;

import org.eclipse.swt.graphics.Image;

import winkhouse.Activator;

public class ImageCache {
	
	public Image immobile = null;
	public Image anagrafica = null;
	public Image anagrafica_immobile = null;
	public Image affitto = null;
	public Image agente = null;
	public Image colloqui = null;
	public Image classe_energetica = null;
	public Image riscaldamento = null;
	public Image statoconservativo = null;
	public Image allegati = null;
	public Image daticatastali = null;
	public Image stanza = null;
	public Image tipologia_immobile = null;
	public Image immagine = null;
	public Image classe_cliente = null;
	public Image recapiti = null;
	public Image abbinamenti = null;
	public Image criteriricerca = null;
	public Image tipo_stanza = null;
	public Image tipo_recapiti = null;
	public Image colloquiagenti = null;
	public Image colloquioanagrafica = null;
	public Image affittianagrafiche = null;
	public Image affittirate = null;
	public Image affittispese = null;
	public Image entity = null;
	public Image attribute = null;
	public Image attributeValue = null;
	

	private static ImageCache instance = null;
	
	private ImageCache(){
		immobile = Activator.getImageDescriptor("icons/wizardimport/immobile.png").createImage();
		anagrafica = Activator.getImageDescriptor("icons/wizardimport/anagrafica.png").createImage();
		anagrafica_immobile = Activator.getImageDescriptor("icons/wizardimport/anagraficaImmobile.png").createImage();
		affitto = Activator.getImageDescriptor("icons/wizardimport/affitti.png").createImage();
		agente = Activator.getImageDescriptor("icons/wizardimport/looknfeel.png").createImage();
		colloqui = Activator.getImageDescriptor("icons/wizardimport/colloqui16.png").createImage();
		classe_energetica = Activator.getImageDescriptor("icons/wizardimport/ce.jpeg").createImage();
		riscaldamento = Activator.getImageDescriptor("icons/wizardimport/riscaldamento.jpeg").createImage();
		statoconservativo = Activator.getImageDescriptor("icons/wizardimport/statoconservativo.png").createImage();
		allegati = Activator.getImageDescriptor("icons/wizardimport/attach.png").createImage();
		daticatastali = Activator.getImageDescriptor("icons/wizardimport/daticatastali.png").createImage();
		stanza = Activator.getImageDescriptor("icons/wizardimport/stanza.png").createImage();
		tipologia_immobile = Activator.getImageDescriptor("icons/wizardimport/tipiimmobili.png").createImage();
		immagine = Activator.getImageDescriptor("icons/wizardimport/immagini.png").createImage();
		classe_cliente = Activator.getImageDescriptor("icons/wizardimport/classianagrafiche.png").createImage();
		recapiti = Activator.getImageDescriptor("icons/wizardimport/recapiti.png").createImage();
		abbinamenti = Activator.getImageDescriptor("icons/wizardimport/abbinamenti.png").createImage();
		criteriricerca = Activator.getImageDescriptor("icons/wizardimport/criteriricerca.png").createImage();
		tipo_stanza = Activator.getImageDescriptor("icons/wizardimport/tipologiastanza.png").createImage();
		tipo_recapiti = Activator.getImageDescriptor("icons/wizardimport/tiporecapiti.png").createImage();
		colloquiagenti = Activator.getImageDescriptor("icons/wizardimport/colloquiagenti.png").createImage();
		affittianagrafiche = Activator.getImageDescriptor("icons/wizardimport/affittianagrafiche.png").createImage();
		affittirate = Activator.getImageDescriptor("icons/wizardimport/rataaffitto.png").createImage();
		affittispese = Activator.getImageDescriptor("icons/wizardimport/affittispese.png").createImage();
		colloquioanagrafica = Activator.getImageDescriptor("icons/wizardimport/colloquioanagrafica.png").createImage();
		entity = Activator.getImageDescriptor("icons/wizardimport/entity.png").createImage();
		attribute = Activator.getImageDescriptor("icons/wizardimport/campi_personali16.png").createImage();
		attributeValue = Activator.getImageDescriptor("icons/wizardimport/campi_personali_value16.png").createImage();
		
	}

	public static ImageCache getInstance() {
		if (instance == null){
			instance = new ImageCache();
		}
		return instance;
	}
}
