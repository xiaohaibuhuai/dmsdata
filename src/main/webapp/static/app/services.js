MainApp.service('TabService', function() {
    this.addTab = function(title,url) { 

		 var tab = parent.$("#index_tabs");
		 if(tab.tabs('exists', title)){        
			 tab.tabs('select', title);      
		 }else {      
		    var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';        
		    tab.tabs('add',{         
				  title:title,          
				  content:content,      
				  closable:true    
			 });      
		 }  
     }
    
    this.playerView=function(id) {
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		this.addTab(title,url);
	};
	
	this.clubView=function(id) {
		var title = "俱乐部"+id;
		var url = PATH+'/stat/club/clubInfo?id='+id;
		this.addTab(title,url);
	};
     
	this.load = function(){
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
	}
 
});