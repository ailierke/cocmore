//获取项目路径
function getPath(){
	var curWwwPath=document.location.href;
    var pathName=document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName+'/');
}
 //获取cookie中的用户名称[o]  id[1]  [2]类型type
function getUser(){
		if (document.cookie.length > 0) {   
			var productName = "yunzoAdmin";
	        begin = document.cookie.indexOf(productName+"=");   
	        if (begin != -1)      
	        {   
	            begin += productName.length+1;//cookie值的初始位置   
	            end = document.cookie.indexOf(";", begin);//结束位置   
	            if (end == -1) {
	            	  end = document.cookie.length;//没有;则end为字符串结束位置  
	            }	
	            data = unescape(document.cookie.substring(begin, end));
	            return eval(data);
	        }
	    }else{
	    	alertWarring("登陆已超时，请重新登陆");
	    	top.location=getPath();
	    	return false;
	    }
}

/*
 * 检查登录是否超时
 * return true 表示cookie失效 ，已经超时
 */
function checkLoginTimeOut(){
	if(document.cookie != ""){
		return false;
	}else{
		Ext.MessageBox.confirm('确认','登录超时,您是否要重新登录？',function(btn){
			if(btn=='yes'){
				top.location=getPath();
			}
		}); 
		return true;
	}
	
}

//日期验证(生效 失效)
Ext.apply(Ext.form.VTypes, {  
    daterange : function(val, field) {  
        var date = field.parseDate(val);  
        if (!date) {  
            return;  
        }  
        if (field.startDateField  
                && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax  
                        .getTime()))) {  
            var start = Ext.getCmp(field.startDateField);  
            start.setMaxValue(date);  
            start.validate();  
            this.dateRangeMax = date;  
        } else if (field.endDateField  
                && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin  
                        .getTime()))) {  
            var end = Ext.getCmp(field.endDateField);  
            end.setMinValue(date);  
            end.validate();  
            this.dateRangeMin = date;  
        }  
        return true;  
    }  
});

/**判断单元格填写是否为空**/
function isNull(obj){
	for(var i =0;i<obj.length;i++){
		if(null==obj[i] || obj[i] == undefined || ""==obj[i].toString().replace(/(^s*)|(s*$)/g, "")){
			return false;
		}
	}
	return true;
}

function alertWarring(msg, anim) {
	Ext.MessageBox.show({
		title : "提示",
		msg : msg,
		width : 250,
		animEl : anim,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.WARNING
	});
}

function alertInformation(msg) {
	Ext.MessageBox.show({
		title : "提示",
		msg : msg,
		width : 250,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.INFO
	});
}

function alertSuccess(msg, anim){
	Ext.MessageBox.show({
		title : "提示",
		msg : '<font style="color:green">'+msg+'</font>',
		width : 250,
		animEl : anim,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.INFO
	});
}

function alertError(msg, anim) {
	Ext.MessageBox.show({
		title : "提示",
		msg : '<font style="color:red">'+msg+'</font>',
		width : 250,
		animEl : anim,
		buttons : Ext.MessageBox.OK,
		icon : Ext.MessageBox.ERROR
	});
}

function alertOkorCancel(msg, fn){
	Ext.MessageBox.show({
        title: '提示',
        msg: msg,
        width : 250,
        buttons: Ext.MessageBox.YESNO,
        fn: fn,
        icon : Ext.MessageBox.QUESTION
    });
}

Ext.override(Ext.form.RadioGroup, {
    getValue : function() {
        var v;
        this.items.each(function(item) {
            if (item.getValue()) {
                v = item.getRawValue();
                return false;
			}
		});
		return v;
	},
	setValue : function(v) {
        if (this.rendered) {
            this.items.each(function(item) {
                item.setValue(item.getRawValue() == v);
			});
        } else {
            for (k in this.items) {
                this.items[k].checked = this.items[k].inputValue == v;
            }
        }
	}
});
/*组织加载器*/
var treeOrgLoader = new Ext.tree.TreeLoader({});
/*团体加载器*/
var treeGroupLoader = new Ext.tree.TreeLoader({
	dataUrl:getPath() + 'groups/getGroupTree'
});
/*组织根节点*/
var rootOrgNode = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "组织",
	draggable : false// 根节点不容许拖动
});
/*团体根节点*/
var rootGroupNode = new Ext.tree.AsyncTreeNode({
	draggable:false,
	id : "source",
	text : "团体",
	draggable : false// 根节点不容许拖动

});

var treePanel1 =  new Ext.tree.TreePanel({
	title:'组织结构',
	height:600,
	loader : treeOrgLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootOrgNode,
	listeners:{
		click:function(n){//点击事件
			if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
			window.parent.orgId = n.attributes.id;
			treePanel2.loader.dataUrl = getPath() + 'groups/getGroupTree';
			treePanel2.loader.baseParams  ={orgId:window.parent.orgId,typeId:'1'},
			treePanel2.root.reload();
		},
		beforeload:function(node) {//加载前
			treeOrgLoader.dataUrl = getPath() + 'org/getOrgTree'; // 定义每个节点的Loader
		}
	}
});
var groupTool2 = new Ext.Toolbar({
	id:'groupTool2',
	items:[{
		xtype:'textfield',
		emptyText:'请输入团体名称',
		id:'searchGroupName'
	}]
});
var groupTool3 = new Ext.Toolbar({
	id:'groupTool3'
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 1
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			if(i==1){
				break;
			}
			if(null==obj[i] ){continue;}
			groupTool2.add(Ext.util.JSON.decode(obj[i]));
		}
		groupTool2.doLayout();
	}
});
	
var treePanel2 = new Ext.tree.TreePanel({
	height:600,
	title:'团体信息',
	loader:treeGroupLoader,
	rootVisible : false,
	autoScroll : true,
	monitorResize: true,
	root:rootGroupNode,
	tbar:groupTool2,
	listeners:{
		click:groupClick
	}
});
var treePanelTuanti = new Ext.Panel({
	region:'west',
	layout:'fit',
	border:false,
	collapsible : true,
	split : true,
	width:420,
	items:[{
		layout:'column',
		columns: 2,
		items:[
			{columnWidth:0.46,border:false,items:[treePanel1]},
			{columnWidth:0.54,border:false,items:[treePanel2]}
		]
	}]
});

function searchBytuantiNameAndOrgId(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(window.parent.orgId){
		var searchGroupName = Ext.getCmp('searchGroupName').getValue();
		treePanel2.loader.dataUrl = getPath() + 'groups/getGroupTree';
		treePanel2.loader.baseParams  ={orgId:window.parent.orgId,groupName:searchGroupName,type:"search"},
		treePanel2.root.reload();
	}else{
		alertWarring("请先选择组织，再进行操作");
	}
}
//setTimeout("addGroup()", 200);
function addGroup(){
	if(window.parent.orgId){
		var searchGroupName = Ext.getCmp('searchGroupName').getValue();
		treePanel2.loader.dataUrl = getPath() + 'groups/getGroupTree';
		treePanel2.loader.baseParams  ={orgId:window.parent.orgId,typeId:'1',groupName:searchGroupName},
		treePanel2.root.reload();
	}
}
addGroup();
function groupClick(n){
		if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
		window.parent.groupId = n.attributes.id;
		//location.reload();
		store.load({params:{groupId:window.parent.groupId,start:0,limit:LIMIT}});
}

function clickHere(){
	top.location=getPath();
}

function groupState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(stateId==6){
		Ext.Ajax.request({
			url : getPath()+'groups/checkChildrenNode',
			params : {groupId : Ext.getCmp('fid').getValue()},
			success: function (response, options) 
			{
				var data = Ext.util.JSON.decode(response.responseText);
				//查询到无子集(子集均失效)则可以封存
				if(data.object){
					Ext.MessageBox.show({
					    title: '提示',
					    msg: "确认要更新当前团体状态？",
					    width:300,
					    buttons: Ext.MessageBox.YESNO,
					    fn: function(e) {
					    		if(e == 'yes') {
					    			groupAjax(window.parent.groupId,stateId);
					    		}
					    	},
					    icon : Ext.MessageBox.QUESTION
					});
				}else{
					alertError("该团体有下属团体还未封存！");
				}
			}
		});
	}else{
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新当前团体状态？",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			groupAjax(window.parent.groupId,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
}

function groupAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/groups/updateGroupStatus',
		params : {fid : param , status : stateId},
		success: function (result, request){
			var data = Ext.util.JSON.decode(result.responseText);
			if(data.success){
				Ext.MessageBox.show({
					title : "提示",
					msg : "更新成功！",
					width : 250,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
				treePanel2.root.reload();
			}else{
				Ext.MessageBox.show({
					title : "提示",
					msg : data.msg,
					width : 250,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
			}
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}