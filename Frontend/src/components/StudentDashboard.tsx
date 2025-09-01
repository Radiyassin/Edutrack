import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Plus, Users, Calendar, FolderOpen, UserPlus } from "lucide-react";

interface Project {
  id: string;
  title: string;
  description: string;
  members: string[];
  createdAt: string;
  status: "active" | "submitted" | "graded";
  grade?: string;
}

const StudentDashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [projects, setProjects] = useState<Project[]>([]);

    // Charger les projets au montage du composant
    useEffect(() => {
      const loadProjects = async () => {
        try {
          setLoading(true);
          const response = await fetch('http://localhost:8081/projects', {
            credentials: 'include'
          });

          if (!response.ok) {
            throw new Error('Erreur lors du chargement des projets');
          }

          const projectsData: Project[] = await response.json();

          // Transformez les données pour la compatibilité
          const formattedProjects = projectsData.map(project => ({
            ...project,
            members: project.memberIds || [], // Utilisez memberIds pour members
            status: "active" as const, // Défaut
            createdAt: project.createdAt.split(' ')[0] // Gardez seulement la date
          }));

          setProjects(formattedProjects);

        } catch (error) {
          console.error('Erreur:', error);
          setError('Impossible de charger les projets');
        } finally {
          setLoading(false);
        }
      };

      loadProjects();
    }, []);
 useEffect(() => {
    fetch("http://localhost:8081/profile", {
      credentials: "include", // indispensable pour SSO
    })
      .then(async (res) => {
        if (res.status === 403) {
          setError("Accès refusé : utilisateur non autorisé");
          navigate("/login"); // redirige si email pas en base
          return null;
        }
        if (!res.ok) {
          throw new Error("Erreur serveur");
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setUser(data);
        }
      })
      .catch((err) => {
        console.error("Erreur fetch profile:", err);
        setError("Impossible de charger les informations");
      })
      .finally(() => {
        setLoading(false);
      });
  }, [navigate]);

  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isAddMemberModalOpen, setIsAddMemberModalOpen] = useState(false);
  const [selectedProjectId, setSelectedProjectId] = useState<string>("");
  const [newProject, setNewProject] = useState({ title: "", description: "" });
  const [memberEmail, setMemberEmail] = useState("");



  // CREATE PROJECT avec fetch
  const handleCreateProject = async () => {
    try {
      const response = await fetch("http://localhost:8081/projects", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          title: newProject.title,
          description: newProject.description
        }),
      });

      if (!response.ok) {
        throw new Error("Erreur lors de la création du projet");
      }

      const createdProject = await response.json();
      // D'ABORD fermer la modal et reset
      setNewProject({ title: "", description: "" });
       setIsCreateModalOpen(false);
       navigate("/student");

    } catch (error) {
      console.error("Erreur:", error);
      setError("Erreur lors de la création du projet");
    }
  };
  // Fonction pour recharger les projets
  const loadProjects = async () => {
    try {
      const response = await fetch('http://localhost:8081/projects', {
        credentials: 'include'
      });

      if (response.ok) {
        const projectsData = await response.json();
        setProjects(projectsData);
      }
    } catch (error) {
      console.error('Erreur chargement projets:', error);
    }
  };

  const handleAddMember = () => {
    setProjects(projects.map(project =>
      project.id === selectedProjectId
        ? { ...project, members: [...project.members, memberEmail] }
        : project
    ));
    setMemberEmail("");
    setIsAddMemberModalOpen(false);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active": return "bg-tech-teal text-white";
      case "submitted": return "bg-yellow-500 text-white";
      case "graded": return "bg-tech-purple text-white";
      default: return "bg-gray-500 text-white";
    }
  };

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center">Chargement...</div>;
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-tech-purple/5 via-background to-tech-teal/5 p-6">
      <div className="max-w-7xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold">Student Dashboard</h1>
            <p className="text-muted-foreground mt-1">Manage your projects and collaborate with classmates</p>
          </div>
          {user && (
            <div className="flex items-center space-x-3 bg-white rounded-xl shadow px-3 py-2">
              <img
                src={user.avatar_url}
                alt={user.name}
                className="w-10 h-10 rounded-full border"
              />
              <div className="text-sm">
                <div className="font-semibold">{user.name}</div>
                <a
                  href={user.html_url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-xs text-blue-600 hover:underline"
                >
                  {user.login}
                </a>
                <div className="text-xs text-gray-500">{user.email}</div>
              </div>
            </div>
          )}

          <Dialog open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen}>
            <DialogTrigger asChild>
              <Button className="bg-gradient-to-r from-tech-purple to-tech-teal hover:from-tech-purple/90 hover:to-tech-teal/90">
                <Plus className="w-4 h-4 mr-2" />
                New Project
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Create New Project</DialogTitle>
                <DialogDescription>
                  Start a new project and invite classmates to collaborate
                </DialogDescription>
              </DialogHeader>
              <div className="space-y-4">
                <div>
                  <Label htmlFor="title">Project Title</Label>
                  <Input
                    id="title"
                    placeholder="Enter project title"
                    value={newProject.title}
                    onChange={(e) => setNewProject({...newProject, title: e.target.value})}
                  />
                </div>
                <div>
                  <Label htmlFor="description">Description</Label>
                  <Input
                    id="description"
                    placeholder="Brief description of your project"
                    value={newProject.description}
                    onChange={(e) => setNewProject({...newProject, description: e.target.value})}
                  />
                </div>
                <Button
                  onClick={handleCreateProject}
                  className="w-full bg-gradient-to-r from-tech-purple to-tech-teal"
                  disabled={!newProject.title || !newProject.description}
                >
                  Create Project
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        {/* Affichage des erreurs */}
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
            <button
              onClick={() => setError(null)}
              className="ml-4 text-red-800 hover:text-red-900"
            >
              ×
            </button>
          </div>
        )}

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="border-0 shadow-md bg-gradient-to-br from-tech-purple/10 to-tech-purple/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Active Projects</CardTitle>
              <div className="text-2xl font-bold text-tech-purple">
                {projects.filter(p => p.status === "active").length}
              </div>
            </CardHeader>
          </Card>

          <Card className="border-0 shadow-md bg-gradient-to-br from-tech-teal/10 to-tech-teal/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Submitted</CardTitle>
              <div className="text-2xl font-bold text-tech-teal">
                {projects.filter(p => p.status === "submitted").length}
              </div>
            </CardHeader>
          </Card>

          <Card className="border-0 shadow-md bg-gradient-to-br from-green-500/10 to-green-500/5">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground">Graded</CardTitle>
              <div className="text-2xl font-bold text-green-600">
                {projects.filter(p => p.status === "graded").length}
              </div>
            </CardHeader>
          </Card>
        </div>

        {/* Projects Grid */}
        <div className="space-y-4">
          <h2 className="text-xl font-semibold">Your Projects</h2>
          {projects.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-muted-foreground">Aucun projet trouvé. Créez votre premier projet !</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {projects.map((project) => (
                <Card key={project.id} className="border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
                  <CardHeader>
                    <div className="flex justify-between items-start">
                      <div className="space-y-2">
                        <CardTitle className="text-lg">{project.title}</CardTitle>
                        <Badge className={`${getStatusColor(project.status)} text-xs`}>
                          {project.status.charAt(0).toUpperCase() + project.status.slice(1)}
                        </Badge>
                      </div>
                      {project.grade && (
                        <div className="text-right">
                          <div className="text-2xl font-bold text-tech-purple">{project.grade}</div>
                          <div className="text-xs text-muted-foreground">Grade</div>
                        </div>
                      )}
                    </div>
                    <CardDescription>{project.description}</CardDescription>
                  </CardHeader>

                  <CardContent className="space-y-4">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                        <Calendar className="w-4 h-4" />
                        <span>{new Date(project.createdAt).toLocaleDateString()}</span>
                      </div>
                      <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                        <Users className="w-4 h-4" />
                        <span>{project.members.length} members</span>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <div className="text-sm font-medium">Team Members:</div>
                      <div className="flex flex-wrap gap-2">
                        {project.members.map((member, index) => (
                          <div key={index} className="flex items-center space-x-1 bg-muted rounded-full px-2 py-1">
                            <Avatar className="w-5 h-5">
                              <AvatarFallback className="text-xs">
                                {member.split(' ').map(n => n[0]).join('').toUpperCase()}
                              </AvatarFallback>
                            </Avatar>
                            <span className="text-xs">{member}</span>
                          </div>
                        ))}
                      </div>
                    </div>

                    <div className="flex space-x-2">
                      <Button variant="outline" size="sm" className="flex-1">
                        <FolderOpen className="w-4 h-4 mr-1" />
                        Open
                      </Button>

                      <Dialog open={isAddMemberModalOpen} onOpenChange={setIsAddMemberModalOpen}>
                        <DialogTrigger asChild>
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => setSelectedProjectId(project.id)}
                            disabled={project.status !== "active"}
                          >
                            <UserPlus className="w-4 h-4" />
                          </Button>
                        </DialogTrigger>
                        <DialogContent>
                          <DialogHeader>
                            <DialogTitle>Add Team Member</DialogTitle>
                            <DialogDescription>
                              Invite a classmate to join this project
                            </DialogDescription>
                          </DialogHeader>
                          <div className="space-y-4">
                            <div>
                              <Label htmlFor="memberEmail">Student Email</Label>
                              <Input
                                id="memberEmail"
                                type="email"
                                placeholder="student@university.edu"
                                value={memberEmail}
                                onChange={(e) => setMemberEmail(e.target.value)}
                              />
                            </div>
                            <Button
                              onClick={handleAddMember}
                              className="w-full bg-gradient-to-r from-tech-purple to-tech-teal"
                              disabled={!memberEmail}
                            >
                              Send Invitation
                            </Button>
                          </div>
                        </DialogContent>
                      </Dialog>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;