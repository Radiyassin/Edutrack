import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";
import { GraduationCap, Mail, Lock, Github } from "lucide-react";

const LoginPage = () => {
  const handleGoogleLogin = () => {
    // This is where you'll integrate your Google SSO
    console.log("Google SSO login clicked");
  };

  const handleGitLabLogin = () => {
    // This is where you'll integrate your GitLab SSO
    window.location.href = "http://localhost:8081/oauth2/authorization/github";
  };

  const handleEmailLogin = (e: React.FormEvent) => {
    e.preventDefault();
    // This is where you'll handle email/password login
    console.log("Email login submitted");
    // For demo purposes, redirect to student dashboard
    window.location.href = "/student";
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gradient-to-br from-tech-purple/8 via-background to-tech-teal/8">
      <div className="w-full max-w-md space-y-6">
        {/* Header */}
        <div className="text-center space-y-4">
          <div className="flex justify-center">
            <div className="p-3 bg-gradient-to-r from-tech-purple to-tech-teal rounded-full shadow-lg">
              <GraduationCap className="w-8 h-8 text-white" />
            </div>
          </div>
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Welcome Back</h1>
            <p className="text-muted-foreground mt-2">
              Sign in to access your academic portal
            </p>
          </div>
        </div>

        {/* Login Card */}
        <Card className="border-0 shadow-2xl bg-gradient-to-b from-card/95 to-card/90 backdrop-blur-sm">
          <CardHeader className="space-y-1 text-center pb-4">
            <CardTitle className="text-xl">Sign In</CardTitle>
            <CardDescription>
              Choose your preferred sign-in method
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            {/* SSO Buttons */}
            <div className="space-y-3">
              <Button
                onClick={handleGoogleLogin}
                variant="outline"
                className="w-full h-12 text-base font-medium border-2 hover:border-google-red/50 hover:bg-google-red/5 transition-all duration-200"
              >
                <svg className="w-5 h-5 mr-3" viewBox="0 0 24 24">
                  <path
                    fill="#4285F4"
                    d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                  />
                  <path
                    fill="#34A853"
                    d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                  />
                  <path
                    fill="#FBBC05"
                    d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                  />
                  <path
                    fill="#EA4335"
                    d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                  />
                </svg>
                Continue with Google
              </Button>

              <Button
                onClick={handleGitLabLogin}
                variant="outline"
                className="w-full h-12 text-base font-medium border-2 hover:border-gitlab-orange/50 hover:bg-gitlab-orange/5 transition-all duration-200"
              >
                <svg className="w-5 h-5 mr-3" viewBox="0 0 24 24" fill="#FC6D26">
                  <path d="M12 21.42l3.684-11.333h-7.368L12 21.42z"/>
                  <path d="M12 21.42L8.316 10.087H3.825L12 21.42z"/>
                  <path d="M3.825 10.087l-1.714 5.27a.847.847 0 00.308.949L12 21.42l-8.175-11.333z"/>
                  <path d="M3.825 10.087h4.491L6.578 2.58a.424.424 0 00-.808 0L3.825 10.087z"/>
                  <path d="M12 21.42l3.684-11.333h4.491L12 21.42z"/>
                  <path d="M20.175 10.087l1.714 5.27a.847.847 0 01-.308.949L12 21.42l8.175-11.333z"/>
                  <path d="M20.175 10.087h-4.491L17.422 2.58a.424.424 0 01.808 0l1.945 7.507z"/>
                </svg>
                Continue with GitHub
              </Button>
            </div>

            

            {/* Email Form */}
            <form onSubmit={handleEmailLogin} className="space-y-4">
              

              

              
            </form>

            {/* Footer Links */}
            <div className="text-center">
              <div className="text-sm text-muted-foreground">
                Need help?{" "}
                <a href="#" className="text-tech-purple hover:underline">
                  Contact IT Support
                </a>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Footer */}
        <div className="text-center text-xs text-muted-foreground">
          <p>
            By signing in, you agree to our{" "}
            <a href="#" className="text-tech-purple hover:underline">
              Terms of Service
            </a>{" "}
            and{" "}
            <a href="#" className="text-tech-purple hover:underline">
              Privacy Policy
            </a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;