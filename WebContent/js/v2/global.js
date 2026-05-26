	function showMenu() {
		$('.MenuOverlay').addClass('displayBlock');
		$('.MenuOverlay').removeClass('displayNone');
	}
	function hideMenu() {
		$('.MenuOverlay').removeClass('displayBlock');
		$('.MenuOverlay').addClass('displayNone');
	} 
	
	try {
		$(document).on('click', 'table:not(table.backgroudBluePtx)', function (e) {
		       hideMenu();
		    });
		    
		    $(document).on('click', 'img[src="images/logos/logoW.png"]', function (e) {
		       // window.open('http://norfipc.com/');
		     });    

	} catch (e) {
	}
    
    
    function itemMenuBackground(background_class){
//    	$('.mainMenu').css('background', 'url(/webbooking/images/menu/'+"1.jpg"+')');
    	$('.mainMenu').removeClass( "bg_1_MainMenu bg_2_MainMenu bg_3_MainMenu " );
    	$('.mainMenu').addClass(background_class);
    }
