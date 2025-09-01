import { useState } from "react";
import { GraduationCap, Users, BookOpen, LogOut, Home } from "lucide-react";
import { NavLink, useLocation } from "react-router-dom";

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarTrigger,
  useSidebar,
} from "@/components/ui/sidebar";

const studentItems = [
  { title: "Dashboard", url: "/student", icon: Home },
  { title: "My Projects", url: "/student/projects", icon: BookOpen },
  { title: "Team", url: "/student/team", icon: Users },
];

const professorItems = [
  { title: "Dashboard", url: "/professor", icon: Home },
  { title: "All Projects", url: "/professor/projects", icon: BookOpen },
  { title: "Students", url: "/professor/students", icon: Users },
];

interface AppSidebarProps {
  userType: "student" | "professor";
}

export function AppSidebar({ userType }: AppSidebarProps) {
  const { state } = useSidebar();
  const location = useLocation();
  const currentPath = location.pathname;
  const collapsed = state === "collapsed";

  const items = userType === "student" ? studentItems : professorItems;
  const userName = userType === "student" ? "Alex Student" : "Dr. Smith";

  const isActive = (path: string) => currentPath === path;
  const isExpanded = items.some((i) => isActive(i.url));
  const getNavCls = ({ isActive }: { isActive: boolean }) =>
    isActive ? "bg-tech-purple/10 text-tech-purple font-medium border-r-2 border-tech-purple" : "hover:bg-muted/50";

  const handleLogout = () => {
    // This would handle logout logic
    console.log("Logout clicked");
    window.location.href = "/";
  };

  return (
    <Sidebar
      className="border-r bg-card/50 backdrop-blur-sm"
      collapsible="icon"
    >
      <SidebarContent>
        {/* User Profile Section */}
        {!collapsed && (
          <div className="p-4 border-b">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-gradient-to-r from-tech-purple to-tech-teal rounded-full">
                <GraduationCap className="w-6 h-6 text-white" />
              </div>
              <div className="flex-1 min-w-0">
                <div className="font-medium text-sm truncate">{userName}</div>
                <div className="text-xs text-muted-foreground capitalize">{userType}</div>
              </div>
            </div>
          </div>
        )}

        {/* Navigation */}
        <SidebarGroup>
          <SidebarGroupLabel className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
            Navigation
          </SidebarGroupLabel>

          <SidebarGroupContent>
            <SidebarMenu>
              {items.map((item) => (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton asChild>
                    <NavLink 
                      to={item.url} 
                      end 
                      className={({ isActive }) => `${getNavCls({ isActive })} flex items-center space-x-3 px-3 py-2 rounded-lg transition-colors`}
                    >
                      <item.icon className="h-4 w-4 flex-shrink-0" />
                      {!collapsed && <span className="truncate">{item.title}</span>}
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>

        {/* Logout Button */}
        <div className="mt-auto p-4 border-t">
          <SidebarMenuButton
            onClick={handleLogout}
            className="w-full flex items-center space-x-3 px-3 py-2 rounded-lg text-destructive hover:bg-destructive/10 transition-colors"
          >
            <LogOut className="h-4 w-4 flex-shrink-0" />
            {!collapsed && <span>Logout</span>}
          </SidebarMenuButton>
        </div>
      </SidebarContent>
    </Sidebar>
  );
}