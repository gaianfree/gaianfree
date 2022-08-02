package com.softarum.svsa.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import lombok.Getter;
import lombok.Setter;

/**
 * @author murakamiadmin
 *
 */
@Getter
@Setter
@Named(value="consultarRegiaoBean")
@ViewScoped
public class ConsultarRegiaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private MapModel simpleModel;
	  
    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();
          
        //Shared coordinates
        LatLng coord1 = new LatLng(-23.226281, -47.267206); 
        LatLng coord2 = new LatLng(-23.1904179, -47.2749218);
        LatLng coord3 = new LatLng(-23.186959, -47.282521);
        LatLng coord4 = new LatLng(-23.1785073, -47.3145762);
        LatLng coord5 = new LatLng(-23.199936, -47.3070918);
          
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "CRAS Jardim Santa Cruz"));
        simpleModel.addOverlay(new Marker(coord2, "CRAS Jardim das Nações"));
        simpleModel.addOverlay(new Marker(coord3, "CRAS Jardim Independência"));
        simpleModel.addOverlay(new Marker(coord4, "CRAS Jardim Saltense"));
        simpleModel.addOverlay(new Marker(coord5, "CREAS Jardim Três Marias"));
    }
    
    
    /*
     * Configuração com chave (.xhtml)
     *  
     * <script type="text/javascript" 
			async="true" defer="defer" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADm8NJoOhbhwbugyIHNqnJOlb1Zij52Go">
		</script>
		
		<p:gmap center="-23.201701, -47.292833" zoom="13" type="HYBRID" 
    			style="width:100%;height:500px" streetView="true"
    			model="#{consultarRegiaoBean.simpleModel}" />
     */
}