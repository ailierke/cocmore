var LIMIT = 26;
Ext.onReady(function(){	
    //加载树
     var loader = new Ext.tree.TreeLoader({  
   		dataUrl:  getPath() +"systemconfigurationFunction/getFunctionTree"
     });
     
     
     var userTreeSave = function(){
    		var selectRow =  grid.getSelectionModel().getSelected();
    		 if(selectRow){
    			var ids = functionPanel.getChecked("id");
    		 		var myMask = new Ext.LoadMask(Ext.getBody(), {
 							msg : '正在处理......请稍后！',
 							removeMask : true // 完成后移除
 						});
 				 	myMask.show();
 					Ext.Ajax.request({
 						url:'/wedding/usercenter/roleFunction.htm?action=saveFunction',
 						method: 'POST',
 						params:'ids='+ids+'&userId='+selectRow.get("fid"),
 	           			text: "saving...",    
 	           			success: function (result, request ){
 					    		myMask.hide();
 					    		if(result.responseText == '1'){
 		           			   	 	Ext.Msg.alert("消息","保存成功！");
 					    		}else if(result.responseText == '0'){
 		           			   	 	Ext.Msg.alert("消息","保存失败！");
 					    		}
 	           			}
 					});
    		 }else{
    		 	Ext.msg.alert('提示','没有选择用户！');
    		 }
    	}
      
     var functionPanel = new  Ext.tree.TreePanel({
 	    region 			 : 'west',
 		title 			 : '功能点', 
        border     		 : true,
        split      		 : true,  
        animate    		 : true,  
        rootVisible		 : false,
        autoScroll 		 : true,
        autoHeight 		 : false,
 		root       	 	 : root,
 		width			 : 250,
    	tbar 			 : new Ext.Toolbar({
    	items:[{
 				text: '&nbsp;保&nbsp;存&nbsp;',
 				enableToggle: false,
 				iconCls:'common_add',
 				handler: userTreeSave
 			}]
        })
     }); 
      
      //选择用户，加载
    	var treeLoad = function(){
    		var selectRow =  grid.getSelectionModel().getSelected();
    		if(selectRow){
    			loader.dataUrl = "/wedding/usercenter/roleFunction.htm?action=getFunction&userId="+selectRow.get("smanId");
    			functionPanel.loader.dataUrl = "/wedding/usercenter/roleFunction.htm?action=getFunction&userId="+selectRow.get("smanId");
    			functionPanel.root.reload();
    			functionPanel.expandAll();
    		}
    	} 
    	
 	
 	functionPanel.on('checkchange', function(node, checked) {   
 		node.expand();   
 		node.attributes.checked = checked;   
 		node.eachChild(function(child) {   
 			child.ui.toggleCheck(checked);   
 			child.attributes.checked = checked;   
 			child.fireEvent('checkchange', child, checked);   
 		});   
 	}, functionPanel);  

     // set the root node
     var root = new  Ext.tree.AsyncTreeNode({
         text: 'Ext JS',
         draggable:false,
         loader     		 : loader, 
         id:'source'
     });
     functionPanel.setRootNode(root);
  
     root.expand();
     
     /****/
     // 页面数据仓库
     var workerRecords = new Ext.data.Record.create([
 			{name:'fid', type:'string'},
 			{name:'faccount', type:'string'},
 			{name:'fbillState', type:'string'} 
 		]);
 	    var store = new Ext.data.Store({
 	        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
                 root : 'obj',
                 totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
             }, workerRecords),
 	        proxy:new Ext.data.HttpProxy({url:getPath() + 'user/findUserAll'}),
 	        sortInfo: {field:'smanId', direction:'ASC'},
 	        remoteStore:true,    // 是否远程调用数据
 	        remoteSort:true      // 是否远程排序
 	    });
 		
 	    

 		// 加载数据
 	    store.load({params:{start:0, limit:LIMIT}});
 	    
 	    var cm = new Ext.grid.ColumnModel([
 	    	new Ext.grid.RowNumberer(),
 	    	{
 	    		id:'faccount', 
 	    		header: '用户账号', 
 	    		width: 150, 
 	    		sortable: true, 
 	    		dataIndex:'faccount'
             },{
 	    		id:'fbillState', 
 	    		header: '状态', 
 	    		width: 150, 
 	    		sortable: true, 
 	    		dataIndex:'fbillState'
             } 
 	    ]);
 	    
 	    var pagingToolBar = new Ext.PagingToolbar({
             pageSize:LIMIT,
 	        store:store,
 	        displayInfo: true,
       		    displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
   			    emptyMsg: "没有记录",
   			    plugins: new Ext.ux.ProgressBarPager()
         });
 	    // GRID构造
 	    var grid = new Ext.grid.GridPanel({
 	        region : 'center',
 			border: false,
 			columnLines:true,
 			loadMask:true,
     		store: store,
     		frame:false,
     		cm:cm,
     		sm: new Ext.grid.RowSelectionModel({
                 singleSelect: true
             }),
             viewConfig : {
 				forceFit : true
 			},
 			autoScroll:false,
 			enableColumnHide : false, 
 	        bbar: pagingToolBar,
 	        tbar: new Ext.Toolbar({
 	        	items:['用户账号：',
         			{
         				xtype:'textfield',
         				emptyText:'请输入用户账号',
         				id:'searchCondition',
         				width:150,
         				listeners : {
 							'specialkey' : function(field, e) {
 								if(e.getKey() == Ext.EventObject.ENTER) {
 									store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}});
 								}
 							}
 						}
         			},'-',{
         				text: '&nbsp;查&nbsp;询&nbsp;',
         				enableToggle: false,
         				iconCls:'common_search',
         				handler: function(){
         					store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}});
         				}
         			}]
 	        })
 	    });
 	    grid.on("click", function() {
 	    		 treeLoad();
     		});
     
     var displayPanel = new Ext.Viewport({
 		layout : 'border',
 		items : [functionPanel,grid]
 	}); 
     
});