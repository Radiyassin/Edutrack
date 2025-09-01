import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { MessageSquare, Star, Users, Calendar, FolderOpen, GraduationCap, TrendingUp } from "lucide-react";

interface Project {
  id: string;
  title: string;
  description: string;
  students: string[];
  createdAt: string;
  submittedAt?: string;
  status: "active" | "submitted" | "graded";
  grade?: string;
  feedback?: string;
  course: string;
}

const ProfessorDashboard = () => {
  const [projects, setProjects] = useState<Project[]>([
    {
      id: "1",
      title: "Machine Learning Classification",
      description: "Building a classification model for image recognition using TensorFlow",
      students: ["Alice Johnson", "Bob Smith", "Carol Davis"],
      createdAt: "2024-01-15",
      submittedAt: "2024-01-25",
      status: "submitted",
      course: "CS 485 - Machine Learning"
    },
    {
      id: "2",
      title: "Web Development Portfolio",
      description: "Creating a responsive portfolio website with React and modern CSS",
      students: ["David Wilson", "Emma Brown"],
      createdAt: "2024-01-10",
      submittedAt: "2024-01-20",
      status: "graded",
      grade: "A-",
      feedback: "Excellent work on responsive design. Consider adding more interactive elements.",
      course: "CS 340 - Web Development"
    },
    {
      id: "3",
      title: "Database Management System",
      description: "Design and implement a complete database system for a library",
      students: ["Frank Miller", "Grace Lee", "Henry Taylor"],
      createdAt: "2024-01-20",
      status: "active",
      course: "CS 450 - Database Systems"
    },
    {
      id: "4",
      title: "Mobile App Development",
      description: "Cross-platform mobile application using React Native",
      students: ["Ivy Chen", "Jack Anderson"],
      createdAt: "2024-01-18",
      submittedAt: "2024-01-28",
      status: "submitted",
      course: "CS 470 - Mobile Development"
    }
  ]);

  const [selectedProject, setSelectedProject] = useState<Project | null>(null);
  const [isGradeModalOpen, setIsGradeModalOpen] = useState(false);
  const [gradeData, setGradeData] = useState({ grade: "", feedback: "" });
  const [filterStatus, setFilterStatus] = useState<string>("all");

  const handleGradeSubmit = () => {
    if (selectedProject) {
      setProjects(projects.map(project =>
        project.id === selectedProject.id
          ? {
              ...project,
              status: "graded" as const,
              grade: gradeData.grade,
              feedback: gradeData.feedback
            }
          : project
      ));
      setGradeData({ grade: "", feedback: "" });
      setIsGradeModalOpen(false);
      setSelectedProject(null);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active": return "bg-tech-teal text-white";
      case "submitted": return "bg-yellow-500 text-white";
      case "graded": return "bg-tech-purple text-white";
      default: return "bg-gray-500 text-white";
    }
  };

  const getGradeColor = (grade?: string) => {
    if (!grade) return "";
    const letter = grade.charAt(0);
    switch (letter) {
      case "A": return "text-green-600";
      case "B": return "text-blue-600";
      case "C": return "text-yellow-600";
      case "D": return "text-orange-600";
      case "F": return "text-red-600";
      default: return "text-gray-600";
    }
  };

  const filteredProjects = projects.filter(project => 
    filterStatus === "all" || project.status === filterStatus
  );

  const stats = {
    total: projects.length,
    active: projects.filter(p => p.status === "active").length,
    submitted: projects.filter(p => p.status === "submitted").length,
    graded: projects.filter(p => p.status === "graded").length,
    avgGrade: projects.filter(p => p.grade).length > 0 
      ? (projects.filter(p => p.grade).reduce((acc, p) => {
          const gradePoints = p.grade?.charAt(0) === "A" ? 4 : 
                             p.grade?.charAt(0) === "B" ? 3 :
                             p.grade?.charAt(0) === "C" ? 2 :
                             p.grade?.charAt(0) === "D" ? 1 : 0;
          return acc + gradePoints;
        }, 0) / projects.filter(p => p.grade).length).toFixed(1)
      : "N/A"
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-tech-purple/5 via-background to-tech-teal/5 p-6">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold">Professor Dashboard</h1>
            <p className="text-muted-foreground mt-1">Monitor student projects and provide feedback</p>
          </div>
          
          <div className="flex items-center space-x-4">
            <Select value={filterStatus} onValueChange={setFilterStatus}>
              <SelectTrigger className="w-40">
                <SelectValue placeholder="Filter by status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Projects</SelectItem>
                <SelectItem value="active">Active</SelectItem>
                <SelectItem value="submitted">Submitted</SelectItem>
                <SelectItem value="graded">Graded</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
          <Card className="border-0 shadow-md bg-gradient-to-br from-tech-purple/10 to-tech-purple/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Total Projects</CardTitle>
              <div className="text-2xl font-bold text-tech-purple">{stats.total}</div>
            </CardHeader>
          </Card>
          
          <Card className="border-0 shadow-md bg-gradient-to-br from-tech-teal/10 to-tech-teal/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Active</CardTitle>
              <div className="text-2xl font-bold text-tech-teal">{stats.active}</div>
            </CardHeader>
          </Card>
          
          <Card className="border-0 shadow-md bg-gradient-to-br from-yellow-500/10 to-yellow-500/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Submitted</CardTitle>
              <div className="text-2xl font-bold text-yellow-600">{stats.submitted}</div>
            </CardHeader>
          </Card>
          
          <Card className="border-0 shadow-md bg-gradient-to-br from-green-500/10 to-green-500/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Graded</CardTitle>
              <div className="text-2xl font-bold text-green-600">{stats.graded}</div>
            </CardHeader>
          </Card>
          
          <Card className="border-0 shadow-md bg-gradient-to-br from-blue-500/10 to-blue-500/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Avg GPA</CardTitle>
              <div className="text-2xl font-bold text-blue-600">{stats.avgGrade}</div>
            </CardHeader>
          </Card>
        </div>

        {/* Projects Grid */}
        <div className="space-y-4">
          <h2 className="text-xl font-semibold">Student Projects</h2>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {filteredProjects.map((project) => (
              <Card key={project.id} className="border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
                <CardHeader>
                  <div className="flex justify-between items-start">
                    <div className="space-y-2 flex-1">
                      <CardTitle className="text-lg">{project.title}</CardTitle>
                      <div className="flex items-center space-x-2">
                        <Badge className={`${getStatusColor(project.status)} text-xs`}>
                          {project.status.charAt(0).toUpperCase() + project.status.slice(1)}
                        </Badge>
                        <span className="text-sm text-muted-foreground">{project.course}</span>
                      </div>
                    </div>
                    {project.grade && (
                      <div className="text-right">
                        <div className={`text-3xl font-bold ${getGradeColor(project.grade)}`}>
                          {project.grade}
                        </div>
                        <div className="text-xs text-muted-foreground">Grade</div>
                      </div>
                    )}
                  </div>
                  <CardDescription className="line-clamp-2">{project.description}</CardDescription>
                </CardHeader>
                
                <CardContent className="space-y-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                      <Calendar className="w-4 h-4" />
                      <span>Created: {new Date(project.createdAt).toLocaleDateString()}</span>
                    </div>
                    {project.submittedAt && (
                      <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                        <TrendingUp className="w-4 h-4" />
                        <span>Submitted: {new Date(project.submittedAt).toLocaleDateString()}</span>
                      </div>
                    )}
                  </div>
                  
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="text-sm font-medium">Students:</div>
                      <div className="flex items-center space-x-1 text-sm text-muted-foreground">
                        <Users className="w-4 h-4" />
                        <span>{project.students.length}</span>
                      </div>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {project.students.map((student, index) => (
                        <div key={index} className="flex items-center space-x-1 bg-muted rounded-full px-3 py-1">
                          <Avatar className="w-5 h-5">
                            <AvatarFallback className="text-xs">
                              {student.split(' ').map(n => n[0]).join('').toUpperCase()}
                            </AvatarFallback>
                          </Avatar>
                          <span className="text-xs">{student}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  
                  {project.feedback && (
                    <div className="space-y-2">
                      <div className="text-sm font-medium">Feedback:</div>
                      <div className="text-sm text-muted-foreground bg-muted/50 p-3 rounded-lg">
                        {project.feedback}
                      </div>
                    </div>
                  )}
                  
                  <div className="flex space-x-2">
                    <Button variant="outline" size="sm" className="flex-1">
                      <FolderOpen className="w-4 h-4 mr-1" />
                      View Project
                    </Button>
                    
                    {project.status === "submitted" && (
                      <Dialog open={isGradeModalOpen} onOpenChange={setIsGradeModalOpen}>
                        <DialogTrigger asChild>
                          <Button 
                            size="sm"
                            className="bg-gradient-to-r from-tech-purple to-tech-teal hover:from-tech-purple/90 hover:to-tech-teal/90"
                            onClick={() => setSelectedProject(project)}
                          >
                            <Star className="w-4 h-4 mr-1" />
                            Grade
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="max-w-lg">
                          <DialogHeader>
                            <DialogTitle>Grade Project</DialogTitle>
                            <DialogDescription>
                              Provide a grade and feedback for "{selectedProject?.title}"
                            </DialogDescription>
                          </DialogHeader>
                          <div className="space-y-4">
                            <div>
                              <Label htmlFor="grade">Grade</Label>
                              <Select value={gradeData.grade} onValueChange={(value) => setGradeData({...gradeData, grade: value})}>
                                <SelectTrigger>
                                  <SelectValue placeholder="Select grade" />
                                </SelectTrigger>
                                <SelectContent>
                                  <SelectItem value="A+">A+</SelectItem>
                                  <SelectItem value="A">A</SelectItem>
                                  <SelectItem value="A-">A-</SelectItem>
                                  <SelectItem value="B+">B+</SelectItem>
                                  <SelectItem value="B">B</SelectItem>
                                  <SelectItem value="B-">B-</SelectItem>
                                  <SelectItem value="C+">C+</SelectItem>
                                  <SelectItem value="C">C</SelectItem>
                                  <SelectItem value="C-">C-</SelectItem>
                                  <SelectItem value="D+">D+</SelectItem>
                                  <SelectItem value="D">D</SelectItem>
                                  <SelectItem value="F">F</SelectItem>
                                </SelectContent>
                              </Select>
                            </div>
                            <div>
                              <Label htmlFor="feedback">Feedback</Label>
                              <Textarea
                                id="feedback"
                                placeholder="Provide detailed feedback for the students..."
                                value={gradeData.feedback}
                                onChange={(e) => setGradeData({...gradeData, feedback: e.target.value})}
                                rows={4}
                              />
                            </div>
                            <Button 
                              onClick={handleGradeSubmit}
                              className="w-full bg-gradient-to-r from-tech-purple to-tech-teal"
                              disabled={!gradeData.grade || !gradeData.feedback}
                            >
                              Submit Grade
                            </Button>
                          </div>
                        </DialogContent>
                      </Dialog>
                    )}
                    
                    {project.feedback && (
                      <Button variant="outline" size="sm">
                        <MessageSquare className="w-4 h-4" />
                      </Button>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfessorDashboard;