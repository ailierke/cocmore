package com.yunzo.cocmore.core.function.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.relation.RoleList;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunzo.cocmore.core.function.aop.aspect.SystemControllerLog;
//import com.yunzo.cocmore.core.function.controller.YSystemconfigurationFunctionController.Tree;
import com.yunzo.cocmore.core.function.model.mysql.YBasicMember;
import com.yunzo.cocmore.core.function.model.mysql.YBasicOrganization;
import com.yunzo.cocmore.core.function.model.mysql.YBasicSocialgroups;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter;
import com.yunzo.cocmore.core.function.model.mysql.YCharacter.Status;
import com.yunzo.cocmore.core.function.model.mysql.YFunctionentriesFunctionjournalentry;
import com.yunzo.cocmore.core.function.model.mysql.YOrganizationauthorized;
import com.yunzo.cocmore.core.function.model.mysql.YRoleauthorization;
import com.yunzo.cocmore.core.function.model.mysql.YSystemUsers;
import com.yunzo.cocmore.core.function.model.mysql.YSystemconfigurationFunction;
import com.yunzo.cocmore.core.function.service.CharacterService;
import com.yunzo.cocmore.core.function.service.GroupsService;
import com.yunzo.cocmore.core.function.service.MemberServiceI;
import com.yunzo.cocmore.core.function.service.OrganizationService;
import com.yunzo.cocmore.core.function.service.UserService;
import com.yunzo.cocmore.core.function.service.YFunctionentriesFunctionjournalentryService;
import com.yunzo.cocmore.core.function.service.YRoleauthorizationService;
import com.yunzo.cocmore.core.function.service.YSystemconfigurationFunctionService;
import com.yunzo.cocmore.core.function.util.Tree;

/**
 * @ClassName: YRoleauthorizationController 
 * @Description: TODO 角色关系Controller 
 * @date 2014年11月25日 下午4:49:00 
 * @author Ian
 *
 */
@Controller
@RequestMapping("/roleauthorization")
public class YRoleauthorizationController {
private static final Logger logger = Logger.getLogger(YRoleauthorizationController.class);
	
	@Resource(name = "roleauthorizationService")
	private YRoleauthorizationService roleauthorizationService;
	
	@Resource(name = "systemconfigurationFunctionService")
	private YSystemconfigurationFunctionService systemconfigurationFunctionService;
	
	@Resource(name = "functionentriesFunctionjournalentryService")
	private YFunctionentriesFunctionjournalentryService functionentriesFunctionjournalentryService;
	
	@Resource(name = "characterService")
	private CharacterService characterService;
	
	@Resource(name="groupsService")
	GroupsService groupsService;
	
	@Resource(name = "orgService")
	private OrganizationService orgService;
	
	@Resource(name = "userInfo")
	private UserService userService;
	
	@Resource(name = "memberService")
	private MemberServiceI memberService;
	
	/**
	 * 添加给角色授权
	 * @param ids
	 * @param characterId
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	@SystemControllerLog(description="添加给角色授权")
	public Map<String, Object> save(String[] ids,String characterId){
		logger.info("roleauthorizationService save");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			YCharacter character=characterService.getById(characterId);
			List<YFunctionentriesFunctionjournalentry> functionlentryList=null;
			functionlentryList=functionentriesFunctionjournalentryService.findAll();
			List<YRoleauthorization> roleList=new ArrayList<YRoleauthorization>();
			for(String id:ids){
				YFunctionentriesFunctionjournalentry ff=null;
				YRoleauthorization r=new YRoleauthorization();
				r.setYCharacter(character);
				boolean s=true;
				for(YFunctionentriesFunctionjournalentry f:functionlentryList){
					if(f.getFid().equals(id)){
						ff=f;
						s=false;
						break;
					}
				}
				YSystemconfigurationFunction sf=new YSystemconfigurationFunction();
				if(s){
					sf.setFid(id);
					r.setYSystemconfigurationFunction(sf);
					r.setYFunctionentriesFunctionjournalentry(null);
				}else{
					r.setYFunctionentriesFunctionjournalentry(ff);
					r.setYSystemconfigurationFunction(ff.getYSystemconfigurationFunction());
				}
				roleList.add(r);
				//roleauthorizationService.save(r);
			}
			//roleauthorizationService.save(roleauthorization);
			List<YRoleauthorization> rolelist = roleauthorizationService
					.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"
							+ characterId + "' and y.YSystemUsers.fid=null");
			//遍历获取没有的权限并添加
			for(YRoleauthorization role:roleList){
				boolean t=true;
				for(YRoleauthorization ro:rolelist){
					if(role.getYFunctionentriesFunctionjournalentry()==null){
						if(ro.getYFunctionentriesFunctionjournalentry()==null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())){
								t=false;
								break;
							}
						}
					}else{
						if(ro.getYFunctionentriesFunctionjournalentry()!=null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())&&ro.getYFunctionentriesFunctionjournalentry().getFid().equals(role.getYFunctionentriesFunctionjournalentry().getFid())){
								t=false;
								break;
							}
						}
					}
				}
				if(t){
					roleauthorizationService.save(role);
				}
			}
			
			/**
			 * 遍历获取已经取消的权限 并删除
			 */
			for(YRoleauthorization r:rolelist){
				boolean x=true;
				for(YRoleauthorization yr:roleList){
					if(r.getYFunctionentriesFunctionjournalentry()==null){
						if(yr.getYFunctionentriesFunctionjournalentry()==null){
							if(r.getYSystemconfigurationFunction().getFid().equals(yr.getYSystemconfigurationFunction().getFid())){
								x=false;
								break;
							}
						}
					}else{
						if(yr.getYFunctionentriesFunctionjournalentry()!=null){
							if(r.getYSystemconfigurationFunction().getFid().equals(yr.getYSystemconfigurationFunction().getFid())&&yr.getYFunctionentriesFunctionjournalentry().getFid().equals(r.getYFunctionentriesFunctionjournalentry().getFid())){
								x=false;
								break;
							}
						}
					}
				}
				if(x){
					roleauthorizationService.delete(r);
				}
			}
			if(ids!=null&&ids.length>0){
				character.setFprivileges(Status.SAVE.value());
			}else{
				character.setFprivileges(Status.ADDNEW.value());	
			}
			characterService.update(character);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 给用户授予角色
	 * @param jsids
	 * @param userId
	 * @param orgids
	 * @param groupids
	 * @return
	 */
	@RequestMapping(value="/savefunctionRoleauthorization")
	@ResponseBody
	@SystemControllerLog(description="roleauthorizationService savefunctionRoleauthorization")
	public Map<String, Object> savefunctionRoleauthorization(String[] jsids,String userId,String[] orgids,String[] groupids){
		logger.info("roleauthorizationService savefunctionRoleauthorization");
		
//		logger.info("jsids   ################ "+jsids);
//		logger.info("userId ###################"+userId);
//		logger.info("orgids ######################"+orgids);
//		logger.info("groupids #####################"+groupids);
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			YSystemUsers user=userService.getById(userId);
			List<YRoleauthorization> roleList=new ArrayList<YRoleauthorization>();
			if(groupids.length==0 && orgids.length==0&&!"".equals(userId)){
				
				//type==0  //团体     获取会员团体ID 组织ID
				//==1  //	
				
				if(user.getFtypeId().equals("0")){
					List<YBasicMember> memlist=memberService.findByHql("from YBasicMember as y where y.YSystemUsers.fid='"+user.getFid()+"'");
					
					YBasicMember member=memlist.get(0);
					for(String jsid:jsids){
						YCharacter character=new YCharacter();
						character.setFid(jsid);
						List<YRoleauthorization> rlist = roleauthorizationService.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"+jsid+"' and  y.YSystemUsers.fid=null");
						for(YRoleauthorization yr: rlist){
							YRoleauthorization yro=new YRoleauthorization();
							yro.setYSystemUsers(user);
							yro.setYSystemconfigurationFunction(yr.getYSystemconfigurationFunction());
							yro.setYFunctionentriesFunctionjournalentry(yr.getYFunctionentriesFunctionjournalentry());
							yro.setYCharacter(character);
//							yro.setForganizationId(member.getYBasicSocialgroups().getFid());
//							yro.setfGroupsId(member.getYBasicSocialgroups().getYBasicOrganization().getFid());
							yro.setForganizationId(member.getYBasicSocialgroups().getYBasicOrganization().getFid());
							yro.setfGroupsId(member.getYBasicSocialgroups().getFid());
							//roleauthorizationService.save(yro);
							roleList.add(yro);
						}
					}
				}
			}else{
				String orgidsString=null;
				String groupidsString=null;
				for(String s:orgids){
					if(orgidsString==null){
						orgidsString=s;
					}else{
						orgidsString=orgidsString+","+s;
					}
					
				}
				for(String g:groupids){
					if(groupidsString==null){
						groupidsString=g;
					}else{
						groupidsString=groupidsString+","+g;
					}
					
				}
				for(String jsid:jsids){
					YCharacter character=new YCharacter();
					character.setFid(jsid);
					List<YRoleauthorization> rlist= roleauthorizationService.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"+jsid+"' and  y.YSystemUsers.fid=null");
//					System.out.println("333333333333333    "+rlist.size());
					for(YRoleauthorization yr: rlist){
						YRoleauthorization yro=new YRoleauthorization();
						yro.setYSystemUsers(user);
						yro.setYSystemconfigurationFunction(yr.getYSystemconfigurationFunction());
						yro.setYFunctionentriesFunctionjournalentry(yr.getYFunctionentriesFunctionjournalentry());
						yro.setYCharacter(character);
						yro.setForganizationId(orgidsString);
						yro.setfGroupsId(groupidsString);
						//roleauthorizationService.save(yro);
						roleList.add(yro);
					}
				}
			}
			List<YRoleauthorization> rolelist = roleauthorizationService.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"+ userId + "'");
			//获取没有的权限并添加
			for(YRoleauthorization ro:roleList){
				boolean t=true;
				for(YRoleauthorization role:rolelist){
					if(ro.getYFunctionentriesFunctionjournalentry()==null){//判断功能分录是否为空
						if(role.getYFunctionentriesFunctionjournalentry()==null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())){
								if(ro.getfGroupsId()==null){
									if(role.getfGroupsId()==null){
										if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}else{
									if(role.getfGroupsId()!=null){
										if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}
							}
						}
					}else{
						if(role.getYFunctionentriesFunctionjournalentry()!=null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())&&ro.getYFunctionentriesFunctionjournalentry().getFid().equals(role.getYFunctionentriesFunctionjournalentry().getFid())){
								if(ro.getfGroupsId()==null){//判断团体id是否为空
									if(role.getfGroupsId()==null){
										if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}else{
									if(role.getfGroupsId()!=null){
										if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}
							}
						}
					}
				}
				if(t){
					roleauthorizationService.save(ro);
				}
			}
			//判断是否有取消的组织 团体 或 功能点 并删除
			for(YRoleauthorization ro:rolelist){
				boolean t=true;
				for(YRoleauthorization role:roleList){
					if(ro.getYFunctionentriesFunctionjournalentry()==null){//判断功能分录是否为空
						if(role.getYFunctionentriesFunctionjournalentry()==null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())){
								if(ro.getfGroupsId()==null){
									if(role.getfGroupsId()==null){
										if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}else{
									if(role.getfGroupsId()!=null){
										if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}
							}
						}
					}else{
						if(role.getYFunctionentriesFunctionjournalentry()!=null){
							if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())&&ro.getYFunctionentriesFunctionjournalentry().getFid().equals(role.getYFunctionentriesFunctionjournalentry().getFid())){
								if(ro.getfGroupsId()==null){//判断团体id是否为空
									if(role.getfGroupsId()==null){
										if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}else{
									if(role.getfGroupsId()!=null){
										if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
											t=false;
											break;
										}
									}
								}
							}
						}
					}
				}
				if(t){
					roleauthorizationService.delete(ro);
				}
			}
			
			YSystemUsers u=new YSystemUsers();
			u=userService.getById(userId);
			//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和功能都赋予 5待定
			//ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),EFFECT(5);
			if(jsids!=null&&jsids.length>0){
				if(u.getFprivileges()==Status.UNAUDIT.value()){//角色和功能都赋予
					u.setFprivileges(Status.UNAUDIT.value());
				}else if(u.getFprivileges()==Status.AUDIT.value()){//赋予角色权限
					u.setFprivileges(Status.UNAUDIT.value());
				}else{
					u.setFprivileges(Status.SUBMIT.value());
				}
			}else{
				if(u.getFprivileges()==Status.UNAUDIT.value()){
					u.setFprivileges(Status.AUDIT.value());
				}else if(u.getFprivileges()==Status.AUDIT.value()){
					u.setFprivileges(Status.AUDIT.value());
				}else{
					u.setFprivileges(Status.ADDNEW.value());
				}
			}
			userService.update(u);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			map.put("msg", "失败");
			map.put("success", false);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 批量给用户授予角色
	 * @param jsids
	 * @param userId
	 * @param orgids
	 * @param groupids
	 * @return
	 */
	@RequestMapping(value="/saveRoleauthorizations")
	@ResponseBody
	@SystemControllerLog(description="roleauthorizationService savefunctionRoleauthorization")
	public Map<String, Object> saveRoleauthorizations(String[] jsids,String[] userIds,String[] orgids,String[] groupids){
		logger.info("roleauthorizationService savefunctionRoleauthorization");
		
//		logger.info("jsids   ################ "+jsids);
//		logger.info("userId ###################"+userId);
//		logger.info("orgids ######################"+orgids);
//		logger.info("groupids #####################"+groupids);
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			if(userIds!=null&&userIds.length>0){
				for(String userId:userIds){
					YSystemUsers user=userService.getById(userId);
					List<YRoleauthorization> roleList=new ArrayList<YRoleauthorization>();
					if(groupids.length==0 && orgids.length==0&&!"".equals(userId)){
						
						//type==0  //团体     获取会员团体ID 组织ID
						//==1  //	
						
						if(user.getFtypeId().equals("0")){
							List<YBasicMember> memlist=memberService.findByHql("from YBasicMember as y where y.YSystemUsers.fid='"+user.getFid()+"'");
							
							YBasicMember member=memlist.get(0);
							for(String jsid:jsids){
								YCharacter character=new YCharacter();
								character.setFid(jsid);
								List<YRoleauthorization> rlist = roleauthorizationService.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"+jsid+"' and  y.YSystemUsers.fid=null");
								for(YRoleauthorization yr: rlist){
									YRoleauthorization yro=new YRoleauthorization();
									yro.setYSystemUsers(user);
									yro.setYSystemconfigurationFunction(yr.getYSystemconfigurationFunction());
									yro.setYFunctionentriesFunctionjournalentry(yr.getYFunctionentriesFunctionjournalentry());
									yro.setYCharacter(character);
//									yro.setForganizationId(member.getYBasicSocialgroups().getFid());
//									yro.setfGroupsId(member.getYBasicSocialgroups().getYBasicOrganization().getFid());
									yro.setForganizationId(member.getYBasicSocialgroups().getYBasicOrganization().getFid());
									yro.setfGroupsId(member.getYBasicSocialgroups().getFid());
									//roleauthorizationService.save(yro);
									roleList.add(yro);
								}
							}
						}
					}else{
						String orgidsString=null;
						String groupidsString=null;
						for(String s:orgids){
							if(orgidsString==null){
								orgidsString=s;
							}else{
								orgidsString=orgidsString+","+s;
							}
							
						}
						for(String g:groupids){
							if(groupidsString==null){
								groupidsString=g;
							}else{
								groupidsString=groupidsString+","+g;
							}
							
						}
						for(String jsid:jsids){
							YCharacter character=new YCharacter();
							character.setFid(jsid);
							List<YRoleauthorization> rlist= roleauthorizationService.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"+jsid+"' and  y.YSystemUsers.fid=null");
//							System.out.println("333333333333333    "+rlist.size());
							for(YRoleauthorization yr: rlist){
								YRoleauthorization yro=new YRoleauthorization();
								yro.setYSystemUsers(user);
								yro.setYSystemconfigurationFunction(yr.getYSystemconfigurationFunction());
								yro.setYFunctionentriesFunctionjournalentry(yr.getYFunctionentriesFunctionjournalentry());
								yro.setYCharacter(character);
								yro.setForganizationId(orgidsString);
								yro.setfGroupsId(groupidsString);
								//roleauthorizationService.save(yro);
								roleList.add(yro);
							}
						}
					}
					List<YRoleauthorization> rolelist = roleauthorizationService.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"+ userId + "'");
					//获取没有的权限并添加
					for(YRoleauthorization ro:roleList){
						boolean t=true;
						for(YRoleauthorization role:rolelist){
							if(ro.getYFunctionentriesFunctionjournalentry()==null){//判断功能分录是否为空
								if(role.getYFunctionentriesFunctionjournalentry()==null){
									if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())){
										if(ro.getfGroupsId()==null){
											if(role.getfGroupsId()==null){
												if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}else{
											if(role.getfGroupsId()!=null){
												if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}
									}
								}
							}else{
								if(role.getYFunctionentriesFunctionjournalentry()!=null){
									if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())&&ro.getYFunctionentriesFunctionjournalentry().getFid().equals(role.getYFunctionentriesFunctionjournalentry().getFid())){
										if(ro.getfGroupsId()==null){//判断团体id是否为空
											if(role.getfGroupsId()==null){
												if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}else{
											if(role.getfGroupsId()!=null){
												if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}
									}
								}
							}
						}
						if(t){
							roleauthorizationService.save(ro);
						}
					}
					//判断是否有取消的组织 团体 或 功能点 并删除
					for(YRoleauthorization ro:rolelist){
						boolean t=true;
						for(YRoleauthorization role:roleList){
							if(ro.getYFunctionentriesFunctionjournalentry()==null){//判断功能分录是否为空
								if(role.getYFunctionentriesFunctionjournalentry()==null){
									if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())){
										if(ro.getfGroupsId()==null){
											if(role.getfGroupsId()==null){
												if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}else{
											if(role.getfGroupsId()!=null){
												if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}
									}
								}
							}else{
								if(role.getYFunctionentriesFunctionjournalentry()!=null){
									if(ro.getYSystemconfigurationFunction().getFid().equals(role.getYSystemconfigurationFunction().getFid())&&ro.getYFunctionentriesFunctionjournalentry().getFid().equals(role.getYFunctionentriesFunctionjournalentry().getFid())){
										if(ro.getfGroupsId()==null){//判断团体id是否为空
											if(role.getfGroupsId()==null){
												if(ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}else{
											if(role.getfGroupsId()!=null){
												if(ro.getfGroupsId().equals(role.getfGroupsId())&&ro.getForganizationId().equals(role.getForganizationId())&&ro.getYSystemUsers().getFid().equals(role.getYSystemUsers().getFid())){
													t=false;
													break;
												}
											}
										}
									}
								}
							}
						}
						if(t){
							roleauthorizationService.delete(ro);
						}
					}
					
					YSystemUsers u=new YSystemUsers();
					u=userService.getById(userId);
					//y_system_user和y_charater 中FPrivilege 0是未赋权1是赋权 2赋予角色权限 3是赋予用户权限  4是角色和功能都赋予 5待定
					//ADDNEW(0),SAVE(1),SUBMIT(2),AUDIT(3),UNAUDIT(4),EFFECT(5);
					if(jsids!=null&&jsids.length>0){
						if(u.getFprivileges()==Status.UNAUDIT.value()){//角色和功能都赋予
							u.setFprivileges(Status.UNAUDIT.value());
						}else if(u.getFprivileges()==Status.AUDIT.value()){//赋予角色权限
							u.setFprivileges(Status.UNAUDIT.value());
						}else{
							u.setFprivileges(Status.SUBMIT.value());
						}
					}else{
						if(u.getFprivileges()==Status.UNAUDIT.value()){
							u.setFprivileges(Status.AUDIT.value());
						}else if(u.getFprivileges()==Status.AUDIT.value()){
							u.setFprivileges(Status.AUDIT.value());
						}else{
							u.setFprivileges(Status.ADDNEW.value());
						}
					}
					userService.update(u);
				}
				map.put("msg", "成功");
				map.put("success", true);
			}else{
				map.put("msg", "失败");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("msg", "失败");
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 修改
	 * @param roleauthorization
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	@SystemControllerLog(description="roleauthorizationService update")
	public Map<String,Object> update(@ModelAttribute("form")YRoleauthorization roleauthorization){
		logger.info("roleauthorizationService update");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			roleauthorizationService.update(roleauthorization);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 删除
	 * @param roleauthorization
	 * @return
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	@SystemControllerLog(description="roleauthorizationService delete")
	public Map<String,Object> delete(@ModelAttribute("form")YRoleauthorization roleauthorization){
		logger.info("roleauthorizationService delete");
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			roleauthorizationService.delete(roleauthorization);
			map.put("msg", "成功");
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 根据角色获取角色权限
	 * @param characterId
	 * @return
	 */
	@RequestMapping(value="/findByCharacterId")
	@ResponseBody
	@SystemControllerLog(description="根据角色获取角色权限")
	public  Map<String,Object> findByCharacterId(@RequestParam("characterId")String characterId){
		logger.info("roleauthorizationService findByCharacterId");
		Map<String, Object> map = new HashMap<String,Object>();
		List<YRoleauthorization> list=new ArrayList<YRoleauthorization>();
		try {
			list=roleauthorizationService.findByCharacterId(characterId);
			map.put("msg", "成功");
			map.put("obj", list);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 加载角色的功能树
	 * @param jsid
	 * @return
	 */
	@RequestMapping(value="/getByBasicFunctionTree")
	@ResponseBody
	@SystemControllerLog(description="加载角色的功能树")
	public JSONArray getByBasicFunctionTree(String jsid){
		logger.info("roleauthorizationService getByBasicFunctionTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		
		try {
			List<Tree> treelist = new ArrayList<Tree>();
			Map<String,Object> rolemap=new HashMap<String, Object>();
//			List<YFunctionentriesFunctionjournalentry> fflist = new ArrayList<YFunctionentriesFunctionjournalentry>();
//			List<YSystemconfigurationFunction> sflist = new ArrayList<YSystemconfigurationFunction>();
			HashSet<String> ffset=new HashSet<String>();
			HashSet<String> sfset=new HashSet<String>();
			
			rolemap=getroleBycharacterId(jsid);
//			fflist=(List<YFunctionentriesFunctionjournalentry>)rolemap.get("fflist");
//			sflist=(List<YSystemconfigurationFunction>)rolemap.get("sflist");
			
			ffset=(HashSet<String>)rolemap.get("ffset");
			sfset=(HashSet<String>)rolemap.get("sfset");
			
			createTree(treelist,ffset,sfset);
			
			
			
			arrayJson = treeToJson(treelist);
			//writeJson(arrayJson);
			
			map.put("msg", "成功");
			map.put("obj", arrayJson);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "失败");
			map.put("success", false);
		}
		return arrayJson;
	}
	/**
	 * 创建树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	private void createTree(List<Tree> treelist,HashSet<String>ffset,HashSet<String> sfset){
		List<YSystemconfigurationFunction> list = systemconfigurationFunctionService.findByHql("from YSystemconfigurationFunction");
		for(YSystemconfigurationFunction l:list){
			Tree tree = new Tree();
			tree.setId(l.getFid());
			tree.setText(l.getFaccount());
			if(sfset.size()>0){
				for(String id:sfset){
					if(l.getFid().equals(id)){
						tree.setChecked(true);
						break;
					}
				}
			}
			
			createTreechild(l,tree,ffset);
			treelist.add(tree);
		}
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	private void createTreechild(YSystemconfigurationFunction YSystemconfigurationFunction,Tree tree,HashSet<String>ffset){
		List<YFunctionentriesFunctionjournalentry> list=functionentriesFunctionjournalentryService.findByHql("from YFunctionentriesFunctionjournalentry as y where y.YSystemconfigurationFunction.fid='"+YSystemconfigurationFunction.getFid()+"' and y.fIsBasic='1'");
		if(list.size()>0){
			for(YFunctionentriesFunctionjournalentry y:list){
				Tree child = new Tree();
				child.setId(y.getFid());
				child.setText(y.getFfunctionName());
				child.setLeaf(true);
				if(ffset.size()>0){
					for(String id:ffset){
						if(id.equals(y.getFid())){
							child.setChecked(true);
							break;
						}
					}
				}
				
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	
	/**
	 * 
	 * @Title: treeToJson 
	 * @Description: TODO 迭代生成 Tree Json
	 * @param @param tree
	 * @param @return    
	 * @return JSONArray   
	 * @throws
	 */
	private JSONArray treeToJson(List<Tree> tree){
		JSONArray array = new JSONArray();
		for(Tree childrenTree : tree){
			JSONObject object = new JSONObject();
			//JSONObject o = new JSONObject();
			object.put("cls", childrenTree.getCls());
			object.put("id", childrenTree.getId());
			object.put("leaf", childrenTree.getLeaf());
			object.put("text", childrenTree.getText());
			object.put("checked", childrenTree.getChecked());
			
			if(childrenTree.getChildren().size() > 0){
				object.put("children", treeToJson(childrenTree.getChildren()));
			}
			//o.put("children", object);
			array.add(object);
		}
		return array;
	}
	
	
	////****************************************************************************////
	
	/**
	 * 生成团体树的方法
	 * 可以通过组织id和groupName来查询
	 */
	@RequestMapping(value = "/getGroupTree")
	@ResponseBody
	@SystemControllerLog(description="生成团体树的方法")
	public List<Tree> getGroupTree(String[] opids,HttpSession session,String id){
		logger.info("roleauthorizationService getGroupTree");
		List<Tree> treelist = new ArrayList<Tree>();
		String userId=(String)session.getAttribute("userId");

		try {
			HashSet<String> groupset=new HashSet<String>();
			if(null!=id&&!"".equals(id)){
				groupset=getgroupByuserId(id);
			}
			
			
			if(userId.equals("1")){
				for(String opid:opids){
					List<Tree> list = new ArrayList<Tree>();
					if(null!=groupset&&!"".equals(groupset)){
						roleauthorizationService.createTree1(list,opid,userId,null,groupset);
					}else{
						roleauthorizationService.createTree1(list,opid,userId,null,null);
					}
					
					//roleauthorizationService.createTree(list,id);
					for(Tree t:list){
						treelist.add(t);
					}
				}
			}else{
				for(String opid:opids){
					List<Tree> list = new ArrayList<Tree>();
					Map<String, Object> sessionMap = new HashMap<String, Object>();
					sessionMap = (Map<String, Object>)session.getAttribute("map");//从session中获取权限的集合
					HashSet<String> groupSet=(HashSet<String>)sessionMap.get("groupSet");//获取拥有的团体集合
					if(null!=groupset&&!"".equals(groupset)){
						roleauthorizationService.createTree1(list,opid,userId,groupSet,groupset);
					}else{
						roleauthorizationService.createTree1(list,opid,userId,groupSet,null);
					}
					
					//roleauthorizationService.creategroupTree(list,id,groupSet);
					for(Tree t:list){
						treelist.add(t);
					}
				}
			}
			
			//createTree(treelist,orgId,groupName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(treelist);
		return treelist;
	}

/*************************************************************************/	
	/**
	 * 生成组织树的方法
	 */
	@RequestMapping(value = "/getOrgTree")
	@ResponseBody
	@SystemControllerLog(description="生成组织树的方法")
	public JSONArray getOrgTree(HttpSession session,String id){
		logger.info("roleauthorizationService getOrgTree");
		Map<String, Object> map = new HashMap<String,Object>();
		JSONArray arrayJson = new JSONArray();
		String userId=(String)session.getAttribute("userId");
		try {
			HashSet<String> orgset=new HashSet<String>();
			if(id!=null&&!"".equals(id)){
				orgset=getorgByuserId(id);
			}
			
			if(userId.equals("1")){//根据登录用户id判断是否是管理员
				List<Tree> treelist = new ArrayList<Tree>();
				if(orgset!=null&&!"".equals(orgset)){
					createOrgTree(treelist,orgset);
				}else{
					createOrgTree(treelist,null);
				}
				
				arrayJson = treeToJson(treelist);
			}else{
				Map<String, Object> sessionMap = new HashMap<String, Object>();
				sessionMap = (Map<String, Object>)session.getAttribute("map");
				HashSet<String> orgSet=(HashSet<String>)sessionMap.get("orgSet");//根据登录用户获取用户拥有的组织权限
				List<YBasicOrganization> orgList=new ArrayList<YBasicOrganization>();
				for(String fid:orgSet){
					orgList.add(orgService.getById(fid));
				}
				List<Tree> treelist=new ArrayList<Tree>();
				if(orgset!=null&&!"".equals(orgset)){
					createOrgTreeByList(orgList,treelist,orgset);
				}else{
					createOrgTreeByList(orgList,treelist,null);
				}
				
				arrayJson = treeToJson(treelist);
			}
//			map.put("msg", "成功");
//			map.put("obj", arrayJson);
//			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			//map.put("msg", "失败");
			//map.put("success", false);
		}
		return arrayJson;
	}
	/**
	 * 创建组织树的树干
	 * @Title: createTree 
	 * @Description: TODO 
	 * @param     
	 * @return void   
	 * @throws
	 */
	private void createOrgTree(List<Tree> treelist,HashSet<String> orgset){
		List<YBasicOrganization> orglist = orgService.findAll();
		for(YBasicOrganization o:orglist){
			if(o.getFsuperiorOrganizationId()==null||o.getFsuperiorOrganizationId().equals("")){
				Tree tree = new Tree();
				tree.setId(o.getFid());
				tree.setText(o.getFname());
				if(orgset!=null&&!"".equals(orgset)&&orgset.size()>0){
					for(String org:orgset){
						if(o.getFid().equals(org)){
							tree.setChecked(true);
							break;
						}
					}
				}
				createOrgTreechild(o.getFid(),tree,orgset);
				treelist.add(tree);
			}
		}
	}
	/**
	 * 
	 * @Title: createTreechild 
	 * @Description: TODO 创建组织树的子树
	 * @param @param id
	 * @param @param tree    
	 * @return void   
	 * @throws
	 */
	private void createOrgTreechild(String id,Tree tree,HashSet<String> orgset){
		List<YBasicOrganization> list = orgService.getByHql("from YBasicOrganization as y where y.fsuperiorOrganizationId='"+id+"'");
		if(list!=null&&list.size()>0){
			for(YBasicOrganization me : list){
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFname());
				if(orgset!=null&&!"".equals(orgset)&&orgset.size()>0){
					for(String org:orgset){
						if(me.getFid().equals(org)){
							child.setChecked(true);
							break;
						}
					}
				}
				createOrgTreechild(me.getFid(),child,orgset);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	/**
	 * 非管理员的创建树
	 */
	public void createOrgTreeByList(List<YBasicOrganization> orgList,List<Tree> treelist,HashSet<String> orgset){
		for(YBasicOrganization o:orgList){
			boolean t=true;
			for(YBasicOrganization org:orgList){
				if("".equals(o.getFsuperiorOrganizationId())||null==o.getFsuperiorOrganizationId()){
					t=true;
					break;
				}else{
					if(o.getFsuperiorOrganizationId().equals(org.getFid())){
						t=false;
						break;
					}
				}
				
			}
			if(t){
				Tree tree = new Tree();
				tree.setId(o.getFid());
				tree.setText(o.getFname());
				if(orgset!=null&&!"".equals(orgset)&&orgset.size()>0){
					for(String org:orgset){
						if(o.getFid().equals(org)){
							tree.setChecked(true);
							break;
						}
					}
				}
				createOrgTreelist(o.getFid(),tree,orgList,orgset);
				treelist.add(tree);
			}
		}
	
	}
	/**
	 * 非管理员创建子树
	 * @param id
	 * @param tree
	 */
	private void createOrgTreelist(String id,Tree tree,List<YBasicOrganization> orgList,HashSet<String> orgset){
		List<YBasicOrganization> list = new ArrayList<YBasicOrganization>();
		for(YBasicOrganization o:orgList){
			if(!"".equals(o.getFsuperiorOrganizationId())&&null!=o.getFsuperiorOrganizationId()){
				if(o.getFsuperiorOrganizationId().equals(id)){
					list.add(o);
				}
			}
		}
		if(list!=null&&list.size()>0){
			for(YBasicOrganization me : list){
				Tree child = new Tree();
				child.setId(me.getFid());
				child.setText(me.getFname());
				if(orgset!=null&&!"".equals(orgset)&&orgset.size()>0){
					for(String org:orgset){
						if(me.getFid().equals(org)){
							child.setChecked(true);
							break;
						}
					}
				}
				createOrgTreechild(me.getFid(),child,orgset);
				tree.getChildren().add(child);
			}
		}else{
			tree.setLeaf(true);
		}
	}
	/**
	 * 获取角色的权限
	 * @param adminId
	 * @return
	 */
	private Map<String, Object> getroleBycharacterId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<YRoleauthorization> rolelist = roleauthorizationService
				.findByHql("from YRoleauthorization as y where y.YCharacter.fid='"
						+ adminId + "' and y.YSystemUsers.fid=null");
		HashSet<String> ffset=new HashSet<String>();
		HashSet<String> sfset=new HashSet<String>();
		///获取角色权限表中的数据
		for (YRoleauthorization ro : rolelist) {
			if (null == ro.getYFunctionentriesFunctionjournalentry()
					|| "".equals(ro.getYFunctionentriesFunctionjournalentry())) {
				sfset.add(ro.getYSystemconfigurationFunction().getFid());
			} else {
				sfset.add(ro.getYSystemconfigurationFunction().getFid());
				ffset.add(ro.getYFunctionentriesFunctionjournalentry().getFid());
			}
			
		}

		map.put("sfset", sfset);
		map.put("ffset", ffset);
		return map;
	}
	
	/**
	 * 根据用户id查询用户所拥有的组织权限
	 */
	private HashSet<String> getorgByuserId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<YRoleauthorization> rolelist = roleauthorizationService
				.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"
						+ adminId + "'");
		HashSet<String> oset=new HashSet<String>();
		
		///获取角色权限表中的数据
		for (YRoleauthorization ro : rolelist) {
			if(null !=ro.getForganizationId() &&!"".equals(ro.getForganizationId())){
				oset.add(ro.getForganizationId());               //获取组织id
			}
		}
		
		HashSet<String> orgSet=new HashSet<String>();
		
		for(String org:oset){
			String[] o=org.split(",");
			for(String oid:o){
				orgSet.add(oid);
			}
		}
		
		//map.put("orgSet", orgSet);
		// map.put("sflist", removeDuplicate(sflist));
		// map.put("fflist", removeDuplicate(fflist));
		return orgSet;
	}
	/**
	 * 根据用户id查询用户所拥有的团体权限
	 */
	private HashSet<String> getgroupByuserId(String adminId) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<YRoleauthorization> rolelist = roleauthorizationService
				.findByHql("from YRoleauthorization as y where y.YSystemUsers.fid='"
						+ adminId + "'");
		HashSet<String> gset=new HashSet<String>();
		
		///获取角色权限表中的数据
		for (YRoleauthorization ro : rolelist) {
			if(null !=ro.getfGroupsId() &&!"".equals(ro.getfGroupsId())){
				gset.add(ro.getfGroupsId());                   //获取团体id
			}
			
		}
		HashSet<String> groupSet=new HashSet<String>();
		for(String group:gset){
			String[] g=group.split(",");
			for(String gid:g){
				groupSet.add(gid);
			}
		}
		
		// map.put("sflist", removeDuplicate(sflist));
		// map.put("fflist", removeDuplicate(fflist));
		return groupSet;
	}
}
