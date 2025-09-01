import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AppSidebar } from "@/components/AppSidebar";
import Index from "./pages/Index";
import StudentDashboard from "./components/StudentDashboard";
import ProfessorDashboard from "./components/ProfessorDashboard";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Index />} />
          <Route path="/student" element={
            <SidebarProvider>
              <div className="min-h-screen flex w-full">
                <AppSidebar userType="student" />
                <div className="flex-1">
                  <header className="h-12 flex items-center border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
                    <SidebarTrigger className="ml-4" />
                    <div className="ml-4">
                      <h2 className="text-lg font-semibold">Student Portal</h2>
                    </div>
                  </header>
                  <StudentDashboard />
                </div>
              </div>
            </SidebarProvider>
          } />
          <Route path="/professor" element={
            <SidebarProvider>
              <div className="min-h-screen flex w-full">
                <AppSidebar userType="professor" />
                <div className="flex-1">
                  <header className="h-12 flex items-center border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
                    <SidebarTrigger className="ml-4" />
                    <div className="ml-4">
                      <h2 className="text-lg font-semibold">Professor Portal</h2>
                    </div>
                  </header>
                  <ProfessorDashboard />
                </div>
              </div>
            </SidebarProvider>
          } />
          {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;